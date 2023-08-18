package cn.asany.storage.core;

import cn.asany.storage.api.IStorageConfig;
import cn.asany.storage.api.Storage;
import cn.asany.storage.api.StorageBuilder;
import cn.asany.storage.data.domain.StorageConfig;
import cn.asany.storage.data.service.StorageService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfantasy.framework.jackson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

/**
 * 默认存储器解析器
 *
 * @author limaofeng
 */
public class DefaultStorageResolver implements StorageResolver {

  private StorageService storageService;

  private final Map<String, Storage> storages = new HashMap<>();
  private final List<StorageBuilder<?, IStorageConfig>> builders;

  public DefaultStorageResolver(List<StorageBuilder<?, IStorageConfig>> builders) {
    this.builders = builders;
  }

  @Override
  @Transactional(readOnly = true)
  public Storage resolve(String id) {
    if (storages.containsKey(id)) {
      return storages.get(id);
    }
    StorageConfig config = storageService.get(id);
    return resolve(config.getProperties());
  }

  @Override
  public Storage resolve(IStorageConfig config) {
    for (StorageBuilder<?, IStorageConfig> builder : builders) {
      if (builder.supports(config.getClass())) {
        Storage storage = builder.build(config);
        if (storage != null) {
          storages.put(config.getId(), storage);
          return storage;
        }
      }
    }
    throw new FileStoreException("不能创建 IStorage For " + JSON.serialize(config));
  }

  public void setStorageService(@Autowired StorageService storageService) {
    this.storageService = storageService;
  }
}
