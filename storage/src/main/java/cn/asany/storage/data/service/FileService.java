package cn.asany.storage.data.service;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.bean.Space;
import cn.asany.storage.data.bean.StorageConfig;
import cn.asany.storage.data.dao.FileDetailDao;
import cn.asany.storage.data.dao.SpaceDao;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.util.common.DateUtil;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.jfantasy.framework.util.regexp.RegexpUtil;
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
      String description,
      Date lastModified) {
    FileDetail fileDetail =
        FileDetail.builder()
            .path(path)
            .storageConfig(StorageConfig.builder().id(storage).build())
            .name(fileName)
            .mimeType(contentType)
            .length(length)
            .md5(md5)
            .isDirectory(false)
            .lastModified(lastModified)
            .parentFile(createFolder(path.replaceFirst("[^/]+$", ""), storage))
            .description(description)
            .build();
    this.fileDetailDao.save(fileDetail);
    return fileDetail;
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

  private FileDetail createRootFolder(String absolutePath, String managerId) {
    return this.fileDetailDao.save(
        FileDetail.builder()
            .isDirectory(true)
            .path(absolutePath)
            .mimeType(DIR_MIME_TYPE)
            .length(0L)
            .name("")
            .lastModified(DateUtil.now())
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
            .length(0L)
            .md5(DIR_MD5)
            .lastModified(DateUtil.now())
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

  //  /**
  //   * 获取存放文件的绝对路径
  //   *
  //   * @param absolutePath 虚拟目录
  //   * @param fileManagerId 文件管理器Id
  //   * @return {String}
  //   */
  //  public String getAbsolutePath(String absolutePath, String fileManagerId) {
  //    String ext = WebUtil.getExtension(absolutePath);
  //    // absolutePath = RegexpUtil.replace(absolutePath, "[^/]+[.]{0}[^.]{0}$","");
  //    // 去掉后缀名称
  //    if (RegexpUtil.isMatch(absolutePath, "([/][^/]{1,})([.][^./]{1,})$")) {
  //      absolutePath = RegexpUtil.replace(absolutePath, "([/][^/]{1,})([.][^./]{1,})$", "$1");
  //    }
  //    // 可能有效率问题
  //    List<FileDetail> details = new ArrayList<>(); // this.fileDetailDao.find(new
  //    // Criterion[]{Restrictions.like("absolutePath", absolutePath,
  //    // MatchMode.START), Restrictions.eq("namespace", fileManagerId)},
  //    // "absolutePath", "asc");
  //    if (details.isEmpty()
  //        || ObjectUtil.find(details, "absolutePath", absolutePath + "." + ext) == null) {
  //      return absolutePath + "." + ext;
  //    }
  //    for (int i = 1; i <= details.size(); i++) {
  //      if (ObjectUtil.find(details, "absolutePath", absolutePath + "(" + i + ")." + ext) == null)
  // {
  //        return absolutePath + "(" + i + ")." + ext;
  //      }
  //    }
  //    return absolutePath;
  //  }

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
}
