package cn.asany.storage.core;

import cn.asany.storage.api.IStorageConfig;
import cn.asany.storage.api.Storage;
import cn.asany.storage.api.StorageBuilder;
import cn.asany.storage.data.bean.StorageConfig;
import cn.asany.storage.data.service.StorageService;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.HashedMap;
import org.jfantasy.framework.jackson.JSON;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 默认存储器解析器
 *
 * @author limaofeng
 */
public class DefaultStorageResolver implements StorageResolver {

  @Autowired private StorageService storageService;

  private Map<IStorageConfig, Storage> storages = new HashedMap();
  private List<StorageBuilder> builders;

  public DefaultStorageResolver(List<StorageBuilder> builders) {
    this.builders = builders;
  }

  @Override
  public Storage resolve(String id) {
    StorageConfig config = storageService.get(id);
    return resolve(config.getProperties());
  }

  @Override
  public Storage resolve(IStorageConfig config) {
    if (storages.containsKey(config)) {
      return storages.get(config);
    }
    for (StorageBuilder builder : builders) {
      if (builder.supports(config.getClass())) {
        Storage storage = builder.build(config);
        if (storage != null) {
          storages.put(config, storage);
          return storage;
        }
      }
    }
    throw new FileStoreException("不能创建 IStorage For " + JSON.serialize(config));
  }
}
