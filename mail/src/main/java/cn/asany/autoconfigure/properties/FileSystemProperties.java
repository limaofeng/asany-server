package cn.asany.autoconfigure.properties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文件系统配置
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileSystemProperties {
  @Builder.Default private String type = "local";
  private String workingDirectory;
}
