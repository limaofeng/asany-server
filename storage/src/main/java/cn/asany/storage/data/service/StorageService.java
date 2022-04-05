package cn.asany.storage.data.service;

import cn.asany.storage.api.FileObject;
import cn.asany.storage.api.Storage;
import cn.asany.storage.core.StorageResolver;
import cn.asany.storage.data.bean.FileDetail;
import cn.asany.storage.data.bean.StorageConfig;
import cn.asany.storage.data.bean.enums.StorageType;
import cn.asany.storage.data.dao.FileDetailDao;
import cn.asany.storage.data.dao.StorageConfigDao;
import java.util.*;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.Hibernate;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.spring.SpringBeanUtils;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.toys.CompareResults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 存储服务
 *
 * @author limaofeng
 */
@Service
@Transactional
public class StorageService {

  private final StorageConfigDao storageConfigDao;
  private final FileDetailDao fileDetailDao;

  public StorageService(StorageConfigDao storageConfigDao, FileDetailDao fileDetailDao) {
    this.storageConfigDao = storageConfigDao;
    this.fileDetailDao = fileDetailDao;
  }

  public List<StorageConfig> findAll() {
    return storageConfigDao.findAll();
  }

  @Cacheable(key = "targetClass + '.' + methodName + '#' + #p0", value = "STORAGE")
  public StorageConfig get(String id) {
    return Hibernate.unproxy(this.storageConfigDao.getById(id), StorageConfig.class);
  }

  public StorageConfig save(StorageConfig storage) {
    return this.storageConfigDao.save(storage);
  }

  public StorageConfig save(
      StorageType type, String id, String name, String description, Map<String, String> params) {
    StorageConfig storageConfig = new StorageConfig();
    storageConfig.setId(id);
    storageConfig.setName(name);
    storageConfig.setDescription(description);
    storageConfig.setType(type);
    return this.save(storageConfig);
  }

  public List<StorageConfig> getAll() {
    return storageConfigDao.findAll();
  }

  public void delete(String[] ids) {
    for (String id : ids) {
      this.storageConfigDao.deleteById(id);
    }
  }

  public Optional<StorageConfig> findById(String id) {
    return this.storageConfigDao.findById(id);
  }

  public long totalFiles(String id) {
    return this.storageConfigDao.totalFiles(id);
  }

  public long usage(String id) {
    return this.storageConfigDao.usage(id);
  }

  public void reindex(String id) {
    StorageResolver storageResolver = SpringBeanUtils.getBeanByType(StorageResolver.class);
    Storage storage = storageResolver.resolve(id);

    FileObject root = storage.getFileItem(FileObject.ROOT_PATH);

    ReindexContext context =
        ReindexContext.builder()
            .storageId(id)
            .currentFile(FileDetail.builder().path(root.getPath()).build())
            .fileDetailDao(this.fileDetailDao)
            .build();

    reindex(root, context);
    context.submit();
  }

  private void reindex(FileObject currentFile, ReindexContext context) {
    List<FileObject> fileObjects = currentFile.listFiles();

    context.exec(fileObjects);

    for (FileObject item : fileObjects) {
      if (item.isDirectory()) {
        context.setCurrentFile(item.getPath());
        reindex(item, context);
      }
    }
  }

  public Pager<FileDetail> findPager(Pager<FileDetail> pager, List<PropertyFilter> filters) {
    return this.fileDetailDao.findPager(pager, filters);
  }

  public Optional<FileDetail> findOneByPath(String id, String path) {
    return this.fileDetailDao.findOne(
        PropertyFilter.builder().equal("storageConfig.id", id).equal("path", path).build());
  }

  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Data
  private static class ReindexContext {
    private String storageId;
    private FileDetail currentFile;
    @Builder.Default private List<FileDetail> saveBuffer = new ArrayList<>();
    @Builder.Default private List<FileDetail> updateBuffer = new ArrayList<>();
    @Builder.Default private List<FileDetail> deleteBuffer = new ArrayList<>();
    private FileDetailDao fileDetailDao;

    public void exec(List<FileObject> fileObjects) {
      boolean isRoot = FileObject.ROOT_PATH.equals(currentFile.getPath());

      PropertyFilterBuilder filterBuilder =
          PropertyFilter.builder().equal("storageConfig.id", storageId);

      if (isRoot) {
        filterBuilder.isNull("parentFile");
      } else {
        filterBuilder.equal("parentFile.path", currentFile.getPath());
      }

      List<FileObject> alreadyExisted =
          this.fileDetailDao.findAll(filterBuilder.build()).stream()
              .map((item) -> item.toFileObject())
              .collect(Collectors.toList());

      CompareResults<FileObject> results =
          ObjectUtil.compare(
              alreadyExisted, fileObjects, Comparator.comparing(FileObject::getPath));

      FileDetail parentFile = isRoot ? null : this.getCurrentFile();
      for (FileObject fileObject : results.getExceptB()) {
        FileDetail.FileDetailBuilder builder =
            FileDetail.builder()
                .path(fileObject.getPath())
                .name(fileObject.getName())
                .isDirectory(fileObject.isDirectory())
                .size(fileObject.getSize())
                .md5(fileObject.getMetadata().getETag())
                .parentFile(parentFile)
                .storageConfig(StorageConfig.builder().id(storageId).build())
                .mimeType(fileObject.getMimeType());
        FileDetail fileDetail = builder.build();
        fileDetail.setCreatedAt(fileObject.lastModified());
        fileDetail.setUpdatedAt(fileObject.lastModified());
        this.insert(fileDetail);
      }

      for (FileObject fileObject : results.getExceptA()) {
        this.delete((FileDetail) fileObject);
      }
    }

    private void delete(FileDetail file) {
      this.deleteBuffer.add(file);
      if (this.deleteBuffer.size() >= ComplexJpaRepository.BATCH_SIZE) {
        fileDetailDao.deleteAll(this.deleteBuffer);
        this.deleteBuffer.clear();
      }
    }

    private void insert(FileDetail file) {
      this.saveBuffer.add(file);
      if (this.saveBuffer.size() >= ComplexJpaRepository.BATCH_SIZE) {
        fileDetailDao.saveAllInBatch(this.saveBuffer);
        this.saveBuffer.clear();
      }
    }

    public void submit() {
      if (!this.saveBuffer.isEmpty()) {
        fileDetailDao.saveAllInBatch(this.saveBuffer);
      }
      if (!this.deleteBuffer.isEmpty()) {
        fileDetailDao.deleteAll(this.deleteBuffer);
      }
    }

    public void setCurrentFile(String path) {
      this.currentFile = findByPath(path);
    }

    public FileDetail findByPath(String path) {
      if (FileObject.ROOT_PATH.equals(path)) {
        return null;
      }
      FileDetail file = ObjectUtil.find(this.saveBuffer, "path", path);
      if (file == null) {
        file = ObjectUtil.find(this.updateBuffer, "path", path);
      }
      if (file == null) {
        file =
            fileDetailDao
                .findOne(
                    PropertyFilter.builder()
                        .equal("storageConfig.id", storageId)
                        .equal("path", path)
                        .build())
                .orElse(null);
      }
      return file;
    }
  }
}
