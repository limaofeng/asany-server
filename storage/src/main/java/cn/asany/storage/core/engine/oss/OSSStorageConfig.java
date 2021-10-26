package cn.asany.storage.core.engine.oss;

import cn.asany.storage.core.AbstractStorageConfig;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OSSStorageConfig extends AbstractStorageConfig {
  private String accessKeyId;
  private String accessKeySecret;
  private String endpoint;
  private String bucketName;
}
