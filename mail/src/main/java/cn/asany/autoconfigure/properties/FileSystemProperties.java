package cn.asany.autoconfigure.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 文件系统配置
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "james.filesystem")
public class FileSystemProperties {
  @Builder.Default private String type = "local";
  private String workingDirectory;
}
