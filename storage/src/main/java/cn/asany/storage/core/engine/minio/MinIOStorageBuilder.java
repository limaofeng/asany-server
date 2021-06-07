package cn.asany.storage.core.engine.minio;

import cn.asany.storage.core.StorageBuilder;
import org.springframework.stereotype.Component;

@Component
public class MinIOStorageBuilder implements StorageBuilder<MinIOStorage, MinIOStorageConfig> {
    @Override
    public boolean supports(Class<MinIOStorageConfig> config) {
        return MinIOStorageConfig.class.isAssignableFrom(config);
    }

    @Override
    public MinIOStorage build(MinIOStorageConfig config) {
        return new MinIOStorage(config.getEndpoint(), config.getAccessKeyId(), config.getAccessKeySecret(), config.getBucketName(), config.isUseSSL());
    }
}
