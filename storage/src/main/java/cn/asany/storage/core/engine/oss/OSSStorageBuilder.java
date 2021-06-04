package cn.asany.storage.core.engine.oss;


import cn.asany.storage.core.StorageBuilder;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng
 */
@Component
public class OSSStorageBuilder implements StorageBuilder<OSSIStorage, OSSStorageConfig> {

    @Override
    public boolean supports(Class<OSSStorageConfig> config) {
        return OSSStorageConfig.class.isAssignableFrom(config);
    }

    @Override
    public OSSIStorage build(OSSStorageConfig config) {
        String accessKeyId = config.getAccessKeyId();
        String accessKeySecret = config.getAccessKeySecret();
        String endpoint = config.getEndpoint();
        String bucketName = config.getBucketName();
        return new OSSIStorage(endpoint, new OSSIStorage.AccessKey(accessKeyId, accessKeySecret), bucketName);
    }
}
