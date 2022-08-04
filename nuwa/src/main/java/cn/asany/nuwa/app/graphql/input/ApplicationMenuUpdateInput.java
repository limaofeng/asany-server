package cn.asany.nuwa.app.graphql.input;

import cn.asany.nuwa.app.domain.enums.MenuType;
import java.util.Set;
import lombok.Data;

/**
 * 菜单更新对象
 *
 * @author limaofeng
 */
@Data
public class ApplicationMenuUpdateInput {
  /** 名称 */
  private String name;
  /** 对应的图标 */
  private String icon;
  /** 菜单类型 */
  private MenuType type;
  /** 路径 */
  private String path;
  /** 必须登录 */
  private Boolean authorized;
  /** 需要提供的权限 */
  private Set<String> authority;
  /** 父路由 */
  private Long parentMenu;
  /** 默认位置 */
  private Long index;
  /** 隐藏菜单 */
  private Boolean hideInMenu;
  /** 隐藏子菜单 */
  private Boolean hideChildrenInMenu;
  /** 不在面包屑中显示菜单 */
  private Boolean hideInBreadcrumb;
  /** 组件 */
  private Long component;
}
