package cn.asany.base.sms;

import java.util.Map;

/**
 * 短信服务提供商
 *
 * @author limaofeng
 */
public interface ShortMessageServiceProvider {

  /**
   * 配置短信服务提供商
   *
   * @param config 短信服务提供商配置
   */
  void configure(SMSProviderConfig config) throws Exception;

  /**
   * 发送短信
   *
   * @param template 短信模板
   * @param params 模板参数
   * @param sign 签名
   * @param phones 手机号
   * @return 发送结果
   * @throws SendFailedException 发送失败异常
   */
  String send(String template, Map<String, String> params, String sign, String... phones)
      throws SendFailedException;
}
