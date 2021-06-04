package cn.asany.storage.core.engine.oss;

import cn.asany.storage.core.AbstractStorageConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OSSStorageConfig extends AbstractStorageConfig {
    private String accessKeyId;
    private String accessKeySecret;
    private String endpoint;
    private String bucketName;

}
