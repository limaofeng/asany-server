package cn.asany.security.core.bean.enums;

/**
 * 角色类型
 *
 * @author limaofeng
 */
public enum RoleType {
  /** 可以直接授权使用 */
  CLASSIC,
  /** 需要结合 RolePlay 使用 */
  SURFACE
}
