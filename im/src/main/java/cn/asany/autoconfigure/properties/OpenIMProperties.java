package cn.asany.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "openim")
public class OpenIMProperties {
  private String url;
  private String secret;
  private AdminUser admin;
}
