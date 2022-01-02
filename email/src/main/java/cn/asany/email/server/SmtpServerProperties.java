package cn.asany.email.server;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/** @author limaofeng */
@ConfigurationProperties(prefix = "smtp.server")
@Data
public class SmtpServerProperties {

  private String softwareName = "Spring Boot SMTP Server";

  private int port = 10025;

  private int timeout = 120;
}
