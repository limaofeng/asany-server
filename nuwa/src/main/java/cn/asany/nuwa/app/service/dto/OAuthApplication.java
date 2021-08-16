package cn.asany.nuwa.app.service.dto;

import lombok.Data;

/**
 * OAUTH APP
 *
 * @author limaofeng
 */
@Data
public class OAuthApplication {
  private String name;
  private String url;
  private String description;
  private String callbackUrl;
}
