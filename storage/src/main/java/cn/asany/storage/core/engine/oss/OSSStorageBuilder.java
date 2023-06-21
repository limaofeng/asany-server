package cn.asany.storage.core.engine.oss;

import cn.asany.storage.api.IStorageConfig;
import cn.asany.storage.api.StorageBuilder;
import org.springframework.stereotype.Component;

/**
 * 阿里云 OSS 存储构建器
 *
 * @author limaofeng
 */
@Component
public class OSSStorageBuilder implements StorageBuilder<OSSStorage, OSSStorageConfig> {

  @Override
  public boolean supports(Class<? extends IStorageConfig> config) {
    return OSSStorageConfig.class.isAssignableFrom(config);
  }

  @Override
  public OSSStorage build(OSSStorageConfig config) {
    String accessKeyId = config.getAccessKeyId();
    String accessKeySecret = config.getAccessKeySecret();
    String endpoint = config.getEndpoint();
    String bucketName = config.getBucketName();
    return new OSSStorage(config.getId(), endpoint, accessKeyId, accessKeySecret, bucketName);
  }
}
