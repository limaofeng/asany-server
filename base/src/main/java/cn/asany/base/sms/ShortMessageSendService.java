package cn.asany.base.sms;

import java.util.Map;

/**
 * 短信发送接口
 *
 * @author 李茂峰
 * @version 1.0
 */
public interface ShortMessageSendService {

  String DEFAULT_PROVIDER = "aliyun.system_default";

  /**
   * 发送短信
   *
   * @param template 短信模板
   * @param params 模板参数
   * @param sign 签名
   * @param delay 延迟发送
   * @param phones 手机号
   * @return 发送结果
   */
  ShortMessageInfo send(
      String template, Map<String, String> params, String sign, long delay, String... phones);

  /**
   * 发送短信
   *
   * @param provider 短信服务提供商
   * @param template 短信模板
   * @param params 模板参数
   * @param sign 签名
   * @param phones 手机号
   * @return 发送结果
   */
  ShortMessageInfo send(
      String provider, String template, Map<String, String> params, String sign, String... phones);

  /**
   * 发送短信
   *
   * @param provider 短信服务提供商
   * @param template 短信模板
   * @param params 模板参数
   * @param sign 签名
   * @param delay 延迟发送
   * @param phones 手机号
   * @return 发送结果
   */
  ShortMessageInfo send(
      String provider,
      String template,
      Map<String, String> params,
      String sign,
      long delay,
      String... phones);
}
