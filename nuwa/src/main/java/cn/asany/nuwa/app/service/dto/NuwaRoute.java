package cn.asany.nuwa.app.service.dto;

import cn.asany.nuwa.app.bean.LayoutSettings;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 路由
 *
 * @author limaofeng
 */
@Data
@NoArgsConstructor
public class NuwaRoute {
  private Long id;
  private String path;
  private String access;
  private Boolean authorized;
  private String redirect;
  private LayoutSettings settings;
  private NuwaComponent component;
  private List<NuwaRoute> routes;

  public void setLayout(Boolean path) {
    if (this.settings == null) {
      this.settings = new LayoutSettings();
    }
    this.settings.setPure(path);
  }

  public void setHideMenu(Boolean hideMenu) {
    if (this.settings == null) {
      this.settings = new LayoutSettings();
    }
    this.settings.setHideMenu(hideMenu);
  }
}
