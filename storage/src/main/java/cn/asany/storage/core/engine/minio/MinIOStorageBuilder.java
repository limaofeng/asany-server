package cn.asany.storage.core.engine.minio;

import cn.asany.storage.api.IStorageConfig;
import cn.asany.storage.api.StorageBuilder;
import org.springframework.stereotype.Component;

/**
 * MinIO 存储构建器
 * @author limaofeng
 */
@Component
public class MinIOStorageBuilder implements StorageBuilder<MinIOStorage, MinIOStorageConfig> {
  @Override
  public boolean supports(Class<? extends IStorageConfig> config) {
    return MinIOStorageConfig.class.isAssignableFrom(config);
  }

  @Override
  public MinIOStorage build(MinIOStorageConfig config) {
    return new MinIOStorage(
        config.getId(),
        config.getEndpoint(),
        config.getAccessKeyId(),
        config.getAccessKeySecret(),
        config.getBucketName(),
        config.isUseSSL());
  }
}
