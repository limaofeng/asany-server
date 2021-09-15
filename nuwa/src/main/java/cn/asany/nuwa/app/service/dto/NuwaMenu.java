package cn.asany.nuwa.app.service.dto;

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
  private String type;
  private String icon;
  private String path;
  private boolean hideInBreadcrumb;
  private List<NuwaMenu> children;
}
