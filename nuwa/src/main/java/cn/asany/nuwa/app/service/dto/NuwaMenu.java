package cn.asany.nuwa.app.service.dto;

import cn.asany.nuwa.app.bean.enums.MenuType;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 导入导出对象
 *
 * @author limaofeng
 */
@Data
@NoArgsConstructor
public class NuwaMenu {
  private String name;
  private MenuType type;
  private String icon;
  private String path;
  private NuwaComponent component;
  private List<NuwaMenu> children;
  private Boolean hideInBreadcrumb;
  private Boolean hideChildrenInMenu;
  private Boolean hideInMenu;
}
