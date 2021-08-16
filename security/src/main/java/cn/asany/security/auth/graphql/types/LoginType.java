package cn.asany.security.auth.graphql.types;

/** @Author: fengmeng @Date: 2019/5/10 14:25 */
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
