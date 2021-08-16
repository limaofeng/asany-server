package cn.asany.storage.data.service;

import cn.asany.storage.data.bean.StorageConfig;
import cn.asany.storage.data.bean.enums.StorageType;
import cn.asany.storage.data.dao.FileManagerConfigDao;
import java.util.List;
import java.util.Map;
import org.jfantasy.framework.spring.SpringContextUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StorageService {

  @Autowired private FileManagerConfigDao fileManagerConfigDao;

  public List<StorageConfig> findAll() {
    return fileManagerConfigDao.findAll();
  }

  public StorageConfig get(String id) {
    return this.fileManagerConfigDao.getOne(id);
  }

  public StorageConfig save(StorageConfig fileManager) {
    return this.fileManagerConfigDao.save(fileManager);
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
    return fileManagerConfigDao.findAll();
  }

  public void delete(String[] ids) {
    for (String id : ids) {
      this.fileManagerConfigDao.deleteById(id);
    }
  }

  public List<StorageConfig> listFileManager() {
    return this.fileManagerConfigDao.findAll(Sort.by("id").ascending());
  }

  public static List<StorageConfig> getFileManagers() {
    return SpringContextUtil.getBeanByType(StorageService.class).listFileManager();
  }
}
