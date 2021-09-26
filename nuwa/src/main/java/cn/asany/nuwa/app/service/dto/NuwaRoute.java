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
  private LayoutSettings layout;
  private NuwaComponent component;
  private List<NuwaRoute> routes;
}
