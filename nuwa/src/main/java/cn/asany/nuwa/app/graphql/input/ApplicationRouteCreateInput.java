package cn.asany.nuwa.app.graphql.input;

import cn.asany.nuwa.app.domain.enums.RouteType;
import lombok.Data;

@Data
public class ApplicationRouteCreateInput {
  /** 名称 */
  private String name;
  /** 是否启用 */
  private Boolean enabled;
  /** 路由类型 */
  private RouteType type;
  /** 路径 */
  private String path;
  /** 必须登录 */
  private Boolean authorized;
  /** 访问权限 */
  private String access;
  /** 在面包屑中隐藏菜单 */
  private Boolean hideInBreadcrumb;
  /** 重定向的路径 */
  private String redirect;
  /** 组件 */
  private Long component;
  /** 对应的图标 */
  private String icon;
  /** 排序 */
  private Integer index;
  /** 父路由 */
  private Long parentRoute;
  /** 应用 */
  private Long application;
  /** 面包屑组件 */
  private Long breadcrumb;
  /** 布局设置 */
  private LayoutSettingsInput layout;
}
