package cn.asany.nuwa.app.graphql.input;

import cn.asany.nuwa.app.bean.enums.MenuType;
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
  /** 应用 */
  private Long application;
}
