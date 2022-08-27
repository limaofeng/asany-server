package cn.asany.base.sms;

/** 验证码服务 */
public interface CaptchaService {

  /**
   * 验证验证码
   *
   * @param configId 配置id
   * @param id 发送id
   * @param code 验证码
   * @return {boolean}
   */
  boolean validateResponseForID(String configId, String id, String code);

  /**
   * 发送验证码
   *
   * @param configId 配置ID
   * @param id 发送id
   * @param phone 手机号
   * @param force 强制发送
   * @return String
   */
  String getChallengeForID(String configId, String id, String phone, boolean force);
}
