package cn.asany.storage.core.engine.minio;

import cn.asany.storage.core.AbstractStorageConfig;
import lombok.*;

/**
 * 存储配置
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MinIOStorageConfig extends AbstractStorageConfig {
  private String endpoint;
  private String accessKeyId;
  private String accessKeySecret;
  private String bucketName;
  private boolean useSSL;
}
