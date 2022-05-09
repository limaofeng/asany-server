package cn.asany.storage.data.service;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.Storage;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.bean.FileLabel;
import cn.asany.storage.data.bean.Space;
import cn.asany.storage.data.bean.StorageConfig;
import cn.asany.storage.data.dao.FileDetailDao;
import cn.asany.storage.data.dao.SpaceDao;
import java.io.IOException;
import java.util.*;
import org.hibernate.Hibernate;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.dao.mybatis.keygen.util.DataBaseKeyGenerator;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.jfantasy.framework.util.web.WebUtil;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 文件服务
 *
 * @author limaofeng
 */
@Service
@Transactional
public class FileService {

  private static final String DIR_MD5 = "1B2M2Y8AsgTpgAmY7PhCfg==";
  private static final String DIR_MIME_TYPE = "application/octet-stream";

  private final FileDetailDao fileDetailDao;
  private final SpaceDao spaceDao;

  private final DataBaseKeyGenerator dataBaseKeyGenerator;
  private final CacheManager cacheManager;

  public FileService(
      CacheManager cacheManager,
      FileDetailDao fileDetailDao,
      SpaceDao spaceDao,
      DataBaseKeyGenerator dataBaseKeyGenerator) {
    this.fileDetailDao = fileDetailDao;
    this.cacheManager = cacheManager;
    this.spaceDao = spaceDao;
    this.dataBaseKeyGenerator = dataBaseKeyGenerator;
  }

  private long nextId() {
    return this.dataBaseKeyGenerator.nextValue("storage_fileobject:id");
  }

  public FileDetail createFile(
      String path,
      String fileName,
      String contentType,
      long length,
      String md5,
      String storage,
      String storePath,
      String description,
      Long parentFolder) {
    FileDetail fileDetail =
        FileDetail.builder()
            .path(path)
            .storageConfig(StorageConfig.builder().id(storage).build())
            .name(fileName)
            .mimeType(contentType)
            .size(length)
            .md5(md5)
            .isDirectory(false)
            .extension(WebUtil.getExtension(fileName, true).toLowerCase())
            .parentFile(this.fileDetailDao.getById(parentFolder))
            .description(description)
            .storePath(storePath)
            .build();
    this.fileDetailDao.save(fileDetail);
    return fileDetail;
  }

  public FileDetail update(Long id, FileDetail file, boolean merge) {
    try {
      file.setId(id);
      return this.fileDetailDao.update(file, merge);
    } finally {
      cacheEvict(file);
    }
  }

  public FileDetail update(FileDetail fileDetail) {
    try {
      return this.fileDetailDao.save(fileDetail);
    } finally {
      cacheEvict(fileDetail);
    }
  }

  @Cacheable(key = "targetClass + '.' + methodName + '#' + #p0", value = "STORAGE")
  public FileDetail getFileById(Long id) {
    FileDetail fileDetail = this.fileDetailDao.getById(id);
    return Hibernate.unproxy(fileDetail, FileDetail.class);
  }

  public void delete(Long id) {
    FileDetail fileDetail = this.fileDetailDao.getById(id);
    this.fileDetailDao.deleteByPath(fileDetail.getPath());
  }

  public synchronized FileDetail getFolderOrCreateIt(String name, Long parentFolder) {
    Optional<FileDetail> folderOptional =
        this.fileDetailDao.findOne(
            PropertyFilter.builder()
                .equal("name", name)
                .equal("parentFile.id", parentFolder)
                .build());
    return folderOptional.orElseGet(() -> createFolder(name, new HashSet<>(), parentFolder));
  }

  public FileDetail createFolder(String name, FileDetail parentFolder) {
    return this.createFolder(name, new HashSet<>(), parentFolder);
  }

  public FileDetail createFolder(String name, Set<FileLabel> labels, Long parentFolder) {
    return this.createFolder(name, labels, fileDetailDao.getById(parentFolder));
  }

  public FileDetail createFolder(String name, Set<FileLabel> labels, FileDetail parentFolder) {
    return this.createFolder(
        name,
        parentFolder,
        FileOptions.builder().labels(labels).storePath(parentFolder.getStorePath()).build());
  }

  public FileDetail createFolder(String name, FileDetail parentFolder, FileOptions options) {
    long id = nextId();
    FileDetail.FileDetailBuilder builder =
        FileDetail.builder()
            .id(id)
            .mimeType(DIR_MIME_TYPE)
            .path(parentFolder.getPath() + id + FileObject.SEPARATOR)
            .isDirectory(true)
            .size(0L)
            .hidden(options.getHidden())
            .md5(DIR_MD5)
            .labels(options.getLabels())
            .parentFile(parentFolder)
            .storePath(options.getStorePath())
            .storage(parentFolder.getStorageConfig().getId())
            .name(name);

    FileDetail fileDetail = builder.build();
    for (FileLabel label : options.getLabels()) {
      label.setFile(fileDetail);
    }

    return this.fileDetailDao.save(fileDetail);
  }

  /**
   * 获取 Folder 对象
   *
   * @param namePath 显示路径
   */
  public FileDetail createFolder(String namePath) {
    String[] names = StringUtil.tokenizeToStringArray(namePath, FileObject.SEPARATOR);
    FileDetail parent = getRootFolderOrCreateIt();
    for (String name : names) {
      parent = getFolderOrCreateIt(name, parent.getId());
    }
    return parent;
  }

  public FileDetail getOneByPath(String path) {
    Optional<FileDetail> file = findByPath(path);
    if (!file.isPresent()) {
      throw new RuntimeException(path + " does not exist ");
    }
    return file.get();
  }

  public Optional<FileDetail> findById(Long id) {
    return this.fileDetailDao.findById(id);
  }

  @Cacheable(key = "targetClass + '.' + methodName + '#' + #p0", value = "STORAGE")
  public Optional<FileDetail> findByPath(String path) {
    return this.fileDetailDao.findOne(PropertyFilter.builder().equal("path", path).build());
  }

  public boolean exists(String name, Long folder, String storage) {
    return this.fileDetailDao.exists(
        PropertyFilter.builder()
            .equal("storageConfig.id", storage)
            .equal("name", name)
            .equal("parentFile.id", folder)
            .build());
  }

  public Optional<FileDetail> findByNamePath(String path) {
    String[] names = StringUtil.tokenizeToStringArray(path, FileObject.SEPARATOR);
    FileDetail parent = null;
    for (String name : names) {
      PropertyFilterBuilder builder = PropertyFilter.builder().equal("name", name);
      if (parent == null) {
        builder.isNull("parentFile");
      } else {
        builder.equal("parentFile.id", path);
      }
      Optional<FileDetail> file = this.fileDetailDao.findOne(builder.build());
      if (!file.isPresent()) {
        return Optional.empty();
      }
      parent = file.get();
    }
    return Optional.ofNullable(parent);
  }

  private FileDetail getRootFolderOrCreateIt() {
    Optional<FileDetail> rootFolder =
        this.fileDetailDao.findOne(
            PropertyFilter.builder().equal("path", FileObject.ROOT_PATH).build());
    return rootFolder.orElseGet(
        () ->
            this.fileDetailDao.save(
                FileDetail.builder()
                    .isDirectory(true)
                    .path(FileObject.ROOT_PATH)
                    .mimeType(DIR_MIME_TYPE)
                    .size(0L)
                    .name("")
                    .md5(DIR_MD5)
                    .storage(Storage.DEFAULT_STORAGE_ID)
                    .storePath("/")
                    .build()));
  }

  public Pager<FileDetail> findPager(Pager<FileDetail> pager, List<PropertyFilter> filters) {
    return this.fileDetailDao.findPager(pager, filters);
  }

  public List<FileDetail> listFolder(String path, String storage, String orderBy) {
    return this.fileDetailDao.findAll(
        PropertyFilter.builder()
            .equal("parentFolder.path", path)
            .equal("isDirectory", true)
            .equal("storageConfig.id", storage)
            .build(),
        Sort.by(orderBy));
  }

  @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
  public List<FileDetail> listFileDetail(String path, String storageId, String orderBy) {
    return this.fileDetailDao.findAll(
        PropertyFilter.builder()
            .equal("folder.path", path)
            .equal("storageConfig.id", storageId)
            .build(),
        Sort.by(orderBy));
  }

  @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true)
  public FileDetail getFileDetailByMd5(String md5, String storageId) {
    List<FileDetail> fileDetails =
        this.fileDetailDao.findAll(
            PropertyFilter.builder()
                .equal("md5", md5)
                .equal("storageConfig.id", storageId)
                .build());
    if (fileDetails.isEmpty()) {
      return null;
    }
    return fileDetails.get(0);
  }

  /**
   * TODO 用来转移文件目录<br>
   * 比如当文章发布时才将图片更新到 http server。一般来说 localization 对应的 FileManager 是不作为上传目录的。只做文件存错。不记录其文件信息
   *
   * @param absolutePath 虚拟目录
   */
  public void localization(String absolutePath) {
    // 1.获取文件源信息
    // 2.获取本地化配置信息
    // 3.开始转移文件
  }

  /**
   * TODO width、height只适用于图片
   *
   * @param absolutePath 虚拟目录
   * @param width 宽
   * @param height 高
   */
  public void localization(String absolutePath, int width, int height) {
    // 1.获取文件源信息
    // 2.获取本地化配置信息
    // 3.压缩图片
    // 4.开始转移文件
  }

  public Space getSpace(String dirKey) throws IOException {
    Optional<Space> space = this.spaceDao.findById(dirKey);
    if (!space.isPresent()) {
      throw new IOException("目录配置[key=" + dirKey + "]未找到!");
    }
    return space.get();
  }

  public long count(List<PropertyFilter> filters) {
    return this.fileDetailDao.count(filters);
  }

  public List<FileDetail> getFileParentsById(Long id) {
    FileDetail fileDetail = this.fileDetailDao.getById(id);
    String[] parents = StringUtil.tokenizeToStringArray(fileDetail.getPath(), "/");
    List<String> newPaths = new ArrayList<>();
    String basePath = "/";
    for (String path : parents) {
      basePath = basePath + path + "/";
      if (basePath.equals(fileDetail.getPath())) {
        continue;
      }
      newPaths.add(basePath);
    }
    return this.fileDetailDao.findAll(PropertyFilter.builder().in("path", newPaths).build());
  }

  public Space createStorageSpace(String id, String name, String path, String storage) {
    Space.SpaceBuilder spaceBuilder = Space.builder().id(id).name(name).plugins(new HashSet<>());
    FileDetail vFolder = this.createFolder(path);
    spaceBuilder.vFolder(vFolder);
    return this.spaceDao.save(spaceBuilder.build());
  }

  public void deleteStorageSpace(String id) {
    Space space = this.spaceDao.getById(id);
    FileDetail rootFolder = space.getVFolder();
    this.spaceDao.deleteById(id);

    cacheEvict(rootFolder);

    this.fileDetailDao.delete(rootFolder);
  }

  public FileDetail renameFile(Long id, String name) {
    FileDetail fileDetail = this.fileDetailDao.getById(id);

    if (name.equals(fileDetail.getName())) {
      return fileDetail;
    }

    if (RegexpUtil.isMatch(name, "[<>|*?,/]")) {
      throw new ValidationException("文件名不能包含以下字符：<,>,|,*,?,,/");
    }

    PropertyFilterBuilder builder =
        PropertyFilter.builder()
            .notEqual("id", id)
            .equal("name", name)
            .equal("parentFile.id", fileDetail.getParentFile().getId());

    if (this.fileDetailDao.exists(builder.build())) {
      throw new ValidationException("重命名失败，文件名被占用");
    }

    if (!fileDetail.getIsDirectory()) {
      fileDetail.setExtension(WebUtil.getExtension(name, true).toLowerCase());
    }

    cacheEvict(fileDetail);

    fileDetail.setName(name);
    return fileDetail;
  }

  public FileDetail createFolder(String name, Long parentFolder) {
    FileDetail parent = this.fileDetailDao.getById(parentFolder);

    if (RegexpUtil.isMatch(name, "[<>|*?,/]")) {
      throw new ValidationException("文件名不能包含以下字符：<,>,|,*,?,,/");
    }

    if (this.fileDetailDao.exists(
        PropertyFilter.builder()
            .equal("parentFile.id", parent.getId())
            .equal("name", name)
            .build())) {
      throw new ValidationException("新建文件夹失败，文件名被占用");
    }

    String path = parent.getPath() + name + "/";

    return createFolder(name, parent);
  }

  public List<FileDetail> findAll(List<PropertyFilter> filters) {
    return this.fileDetailDao.findAll(filters);
  }

  public Integer clearTrash(Long rootFolder) {
    FileDetail recycler = getRecycleBin(rootFolder);

    cacheEvict(recycler.getPath());

    return this.fileDetailDao.clearTrash(recycler.getPath());
  }

  public FileDetail getTempFolder(Long rootFolderId) {
    String name = ".temp";

    FileDetail rootFolder = this.fileDetailDao.getById(rootFolderId);

    Optional<FileDetail> tempFolderOptional =
        this.fileDetailDao.findOne(
            PropertyFilter.builder()
                .equal("parentFile.id", rootFolder.getId())
                .equal("name", FileDetail.NAME_OF_THE_TEMP_FOLDER)
                .equal("labels.name", FileDetail.NAME_OF_THE_TEMP_FOLDER)
                .equal("labels.namespace", FileLabel.SYSTEM_NAMESPACE)
                .build());

    return tempFolderOptional.orElseGet(
        () -> {
          Set<FileLabel> labels = new HashSet<>();
          labels.add(FileLabel.RECYCLE_BIN.build());

          return this.createFolder(
              FileDetail.NAME_OF_THE_TEMP_FOLDER,
              rootFolder,
              FileOptions.builder()
                  .hidden(true)
                  .labels(labels)
                  .storePath(
                      rootFolder.getStorePath()
                          + FileDetail.NAME_OF_THE_TEMP_FOLDER
                          + FileObject.SEPARATOR)
                  .build());
        });
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public FileDetail getRecycleBin(Long rootFolderId) {
    FileDetail rootFolder = this.fileDetailDao.getById(rootFolderId);

    Optional<FileDetail> recycleBinOptional =
        this.fileDetailDao.findOne(
            PropertyFilter.builder()
                .equal("parentFile.id", rootFolder.getId())
                .equal("name", FileDetail.NAME_OF_THE_RECYCLE_BIN)
                .equal("labels.name", FileDetail.NAME_OF_THE_RECYCLE_BIN)
                .equal("labels.namespace", FileLabel.SYSTEM_NAMESPACE)
                .build());

    return recycleBinOptional.orElseGet(
        () -> {
          Set<FileLabel> labels = new HashSet<>();
          labels.add(FileLabel.RECYCLE_BIN.build());
          return this.createFolder(
              FileDetail.NAME_OF_THE_RECYCLE_BIN,
              rootFolder,
              FileOptions.builder().labels(labels).hidden(true).build());
        });
  }

  /**
   * 查找最近的目录
   *
   * @param path 提供的路径
   * @return Optional
   */
  public Optional<FileDetail> closest(String path) {
    Optional<FileDetail> folder;
    do {
      folder = this.fileDetailDao.findOne(PropertyFilter.builder().equal("path", path).build());
      if (folder.isPresent()) {
        break;
      }
      path = path.replaceFirst("[^/]+/$", "");
    } while (!FileObject.ROOT_PATH.equals(path));
    return folder;
  }

  public FileDetail move(FileDetail file, FileDetail folder) {
    FileDetail originalFolder = this.fileDetailDao.getById(file.getParentFile().getId());
    String originalFolderPath = originalFolder.getPath();
    String newFolderPath = folder.getPath();
    String newPath = newFolderPath + file.getPath().substring(originalFolderPath.length());

    this.cacheEvict(file);

    if (file.getIsDirectory()) {
      this.cacheEvict(file.getPath());
      this.fileDetailDao.replacePath(file.getPath(), newPath);
    }

    file.setPath(newPath);
    file.setParentFile(folder);
    file.setHidden(folder.getHidden());
    if (file.getIsDirectory()) {
      this.fileDetailDao.hideFiles(newPath, folder.getHidden());
    }

    return this.fileDetailDao.save(file);
  }

  private void cacheEvict(String path) {
    for (FileDetail subFile :
        this.fileDetailDao.findAll(
            PropertyFilter.builder().startsWith("path", path).notEqual("path", path).build())) {
      cacheEvict(subFile);
    }
  }

  private void cacheEvict(FileDetail file) {
    Cache cache = cacheManager.getCache("STORAGE");

    assert cache != null;

    cache.evictIfPresent(FileService.class + ".getFileById#" + file.getId());
    cache.evictIfPresent(FileService.class + ".findByPath#" + file.getPath());
  }
}
