package cn.asany.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * James 配置
 *
 * @author limaofeng
 */
@Data
@ConfigurationProperties(prefix = "james")
public class JamesProperties {

  private FileSystemProperties filesystem;
}
