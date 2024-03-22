package cn.asany.nuwa.app.service.dto;

import cn.asany.nuwa.app.domain.enums.MenuType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 导入导出对象
 *
 * @author limaofeng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
