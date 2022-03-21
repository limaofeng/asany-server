package cn.asany.storage.data.service;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.bean.Space;
import cn.asany.storage.data.bean.StorageConfig;
import cn.asany.storage.data.dao.FileDetailDao;
import cn.asany.storage.data.dao.SpaceDao;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.jfantasy.framework.util.web.WebUtil;
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

  public FileService(FileDetailDao fileDetailDao, SpaceDao spaceDao) {
    this.fileDetailDao = fileDetailDao;
    this.spaceDao = spaceDao;
  }

  public FileDetail saveFileDetail(
      String path,
      String fileName,
      String contentType,
      long length,
      String md5,
      String storage,
      String description) {
    FileDetail fileDetail =
        FileDetail.builder()
            .path(path)
            .storageConfig(StorageConfig.builder().id(storage).build())
            .name(fileName)
            .mimeType(contentType)
            .size(length)
            .md5(md5)
            .isDirectory(false)
            .extension(WebUtil.getExtension(fileName))
            .parentFile(createFolder(path.replaceFirst("[^/]+$", ""), storage))
            .description(description)
            .build();
    this.fileDetailDao.save(fileDetail);
    return fileDetail;
  }

  public FileDetail update(Long id, FileDetail file, boolean merge) {
    file.setId(id);
    return this.fileDetailDao.update(file, merge);
  }

  public FileDetail update(FileDetail detail) {
    FileDetail fileDetail = this.getOneByPath(detail.getPath());
    fileDetail.setName(detail.getName());
    fileDetail.setDescription(detail.getDescription());
    this.fileDetailDao.save(fileDetail);
    return fileDetail;
  }

  public FileDetail getFolderById(Long id) {
    return this.fileDetailDao.getById(id);
  }

  public Optional<FileDetail> getFolderByPath(String absolutePath, String storageId) {
    return this.fileDetailDao.findOne(
        PropertyFilter.builder()
            .equal("path", absolutePath)
            .equal("isDirectory", true)
            .equal("storageConfig.id", storageId)
            .build());
  }

  public void delete(Long id) {
    this.fileDetailDao.deleteById(id);
  }

  /**
   * 获取 Folder 对象
   *
   * @param path 路径
   * @return {Folder}
   */
  public FileDetail createFolder(String path, String storage) {
    Optional<FileDetail> optional =
        this.fileDetailDao.findOne(
            PropertyFilter.builder()
                .equal("path", path)
                .equal("isDirectory", true)
                .equal("storageConfig.id", storage)
                .build());
    if (optional.isPresent()) {
      return optional.get();
    }
    if (FileObject.ROOT_PATH.equals(path)) {
      return createRootFolder(path, storage);
    } else {
      return createFolder(path, createFolder(path.replaceFirst("[^/]+/$", ""), storage), storage);
    }
  }

  //  public FileDetail findUniqueByMd5(String md5, String managerId) {
  //    //        List<FileDetail> fileDetails =
  //    // this.fileDetailDao.findBy(Restrictions.eq("fileManagerId", managerId),
  // Restrictions.eq("md5",
  //    // md5));
  //    //        return fileDetails.isEmpty() ? null : fileDetails.get(0);
  //    return null;
  //  }

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

  @Cacheable(key = "targetClass + methodName + #p0", value = "STORAGE")
  public Optional<FileDetail> findByPath(String path) {
    return this.fileDetailDao.findOne(PropertyFilter.builder().equal("path", path).build());
  }

  public Optional<FileDetail> findByPath(String storage, String path) {
    return this.fileDetailDao.findOne(
        PropertyFilter.builder().equal("storageConfig.id", storage).equal("path", path).build());
  }

  private FileDetail createRootFolder(String absolutePath, String managerId) {
    return this.fileDetailDao.save(
        FileDetail.builder()
            .isDirectory(true)
            .path(absolutePath)
            .mimeType(DIR_MIME_TYPE)
            .size(0L)
            .name("")
            .storageConfig(StorageConfig.builder().id(managerId).build())
            .md5(DIR_MD5)
            .build());
  }

  /**
   * 获取 Folder 对象
   *
   * @param absolutePath 路径
   * @param parent 上级文件夹
   * @param managerId 文件管理器
   * @return {Folder}
   */
  private FileDetail createFolder(String absolutePath, FileDetail parent, String managerId) {
    FileDetail.FileDetailBuilder builder =
        FileDetail.builder()
            .path(absolutePath)
            .isDirectory(true)
            .mimeType(DIR_MIME_TYPE)
            .size(0L)
            .md5(DIR_MD5)
            .storageConfig(StorageConfig.builder().id(managerId).build())
            .name(RegexpUtil.parseGroup(absolutePath, "([^/]+)\\/$", 1));
    if (ObjectUtil.isNotNull(parent)) {
      builder.parentFile(parent);
    }
    return this.fileDetailDao.save(builder.build());
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
    Space space =
        Space.builder()
            .id(id)
            .path(path)
            .storage(StorageConfig.builder().id(storage).build())
            .name(name)
            .build();
    this.createFolder(path, storage);
    return this.spaceDao.save(space);
  }

  public void deleteStorageSpace(String id) {
    Space space = this.spaceDao.getById(id);
    Optional<FileDetail> rootFolderOptional =
        this.fileDetailDao.findOne(
            PropertyFilter.builder()
                .equal("storageConfig.id", space.getStorage().getId())
                .equal("path", space.getPath())
                .build());
    this.spaceDao.deleteById(id);
    rootFolderOptional.ifPresent(this.fileDetailDao::delete);
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
            .equal("storageConfig.id", fileDetail.getStorageConfig().getId())
            .notEqual("id", id);

    if (fileDetail.getParentFile() == null) {
      builder.isNull("parentFile.id");
    } else {
      builder.equal("parentFile.id", fileDetail.getParentFile().getId());
    }

    builder.equal("name", name);

    if (this.fileDetailDao.exists(builder.build())) {
      throw new ValidationException("重命名失败，文件名被占用");
    }

    fileDetail.setName(name);
    fileDetail.setExtension(WebUtil.getExtension(name));

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

    return createFolder(path, parent.getStorageConfig().getId());
  }

  public List<FileDetail> findAll(List<PropertyFilter> filters) {
    return this.fileDetailDao.findAll(filters);
  }
}
