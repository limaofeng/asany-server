package cn.asany.base.sms;

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
  RESET_PHONE_NEW_PHONE;

  public static final String CAPTCHA_CONFIG_ID = "default";

  public static String getSessionId(String phone, CaptchaSource source) {
    return phone + ":" + source;
  }
}
