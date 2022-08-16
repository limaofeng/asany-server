package cn.asany.sms.graphql.type;

public enum CaptchaSource {
  /** 登录 */
  LOGIN,
  /** 找回密码 */
  RESET_PASSWORD,
  /** 注册 */
  SIGNUP,
  /** 变更手机号码 - 验证旧手机 */
  RESET_PHONE_OLD_PHONE,
  /** 变更手机号码 - 验证新手机 */
  RESET_PHONE_NEW_PHONE
}
