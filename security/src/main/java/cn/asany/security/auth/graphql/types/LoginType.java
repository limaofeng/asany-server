package cn.asany.security.auth.graphql.types;

/**
 * 登录方式
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
public enum LoginType {
  /** 密码登录 */
  password,
  /** 钉钉登录 */
  dingtalk,
  /** 微信公众号登录 */
  WeChatPM,
  /** 企业微信登录 */
  WeChatCP,
  /** 游客 */
  @Deprecated
  tourist,
  /** 通过工号直接登录 */
  @Deprecated
  single
}
