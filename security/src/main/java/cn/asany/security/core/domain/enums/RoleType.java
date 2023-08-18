package cn.asany.security.core.domain.enums;

/**
 * 角色类型
 *
 * @author limaofeng
 */
public enum RoleType {
  /** 可以直接授权使用 */
  CLASSIC,
  /** 需要结合 RolePlay 使用 */
  SURFACE,
  /** 模板角色，不可直接授权，需要通过模板创建角色 */
  TEMPLATE
}
