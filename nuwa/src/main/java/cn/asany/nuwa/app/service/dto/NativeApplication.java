package cn.asany.nuwa.app.service.dto;

import cn.asany.base.IModuleProperties;
import cn.asany.nuwa.app.domain.enums.ApplicationType;
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
  @Builder.Default private ApplicationType type = ApplicationType.Native;
  private String title;
  private String url;
  private String description;
  private String callbackUrl;
  private String setupURL;
  private WebHook webhook;

  private String clientId;
  private String clientSecret;

  private Set<String> authorities;
  private List<NuwaRoute> routes;
  private List<NuwaMenu> menus;

  private List<IModuleProperties> modules;
}
