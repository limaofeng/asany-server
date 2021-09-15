package cn.asany.nuwa.app.service.dto;

import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 原生应用
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NativeApplication {
  private String name;
  private String title;
  private String url;
  private String description;
  private String callbackUrl;
  private String setupURL;
  private WebHook webhook;
  private String routespace;

  private String clientId;
  private String clientSecret;

  private Set<String> authorities;
  private List<NuwaRoute> routes;
  private List<NuwaMenu> menus;
}
