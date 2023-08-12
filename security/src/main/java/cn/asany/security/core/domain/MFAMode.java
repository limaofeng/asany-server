package cn.asany.security.core.domain;

/**
 * 多因素认证模式
 *
 * @author limaofeng
 */
public enum MFAMode {
  FORCE_ALL_USERS, // 强制所有用户
  DEPENDS_ON_USER, // 依赖每个用户的独立配置
  ONLY_ON_EXCEPTION // 仅异常登录时使用
}
