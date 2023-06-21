package cn.asany.storage.core.engine.disk;

import cn.asany.storage.api.IStorageConfig;
import cn.asany.storage.api.StorageBuilder;
import cn.asany.storage.core.FileStoreException;
import org.springframework.stereotype.Component;

/**
 * 本地存储构建器
 * @author limaofeng
 */
@Component
public class LocalStorageBuilder implements StorageBuilder<LocalStorage, LocalStorageConfig> {

  @Override
  public boolean supports(Class<? extends IStorageConfig> config) {
    return LocalStorageConfig.class.isAssignableFrom(config);
  }

  @Override
  public LocalStorage build(LocalStorageConfig config) {
    String defaultDir = config.getDefaultDir();
    if (defaultDir == null) {
      throw new FileStoreException(" LocalFileManager 未配置 defaultDir 项");
    }
    return new LocalStorage(config.getId(), defaultDir);
  }
}
