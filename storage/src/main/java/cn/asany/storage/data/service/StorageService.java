package cn.asany.storage.data.service;

import cn.asany.storage.data.bean.StorageConfig;
import cn.asany.storage.data.bean.enums.StorageType;
import cn.asany.storage.data.dao.FileDetailDao;
import cn.asany.storage.data.dao.StorageConfigDao;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

  public StorageConfig get(String id) {
    return this.storageConfigDao.getById(id);
  }

  public StorageConfig save(StorageConfig storage) {
    return this.storageConfigDao.save(storage);
  }

  public void save(
      StorageType type, String id, String name, String description, Map<String, String> params) {
    StorageConfig fileManagerConfig = new StorageConfig();
    fileManagerConfig.setId(id);
    fileManagerConfig.setName(name);
    fileManagerConfig.setDescription(description);
    fileManagerConfig.setType(type);
    for (Map.Entry<String, String> entry : params.entrySet()) {
      //            fileManagerConfig.addConfigParam(entry.getKey(), entry.getValue());
    }
    this.save(fileManagerConfig);
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
}
