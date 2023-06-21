package cn.asany.base.sms;

/**
 * 短信服务提供商配置
 *
 * @author limaofeng
 */
public interface SMSProviderConfig {
  /**
   * 获取短信服务提供商标识
   *
   * @return 短信服务提供商标识
   */
  String getKey();
  /**
   * 获取短信服务提供商名称
   *
   * @return 短信服务提供商名称
   */
  SMSProvider getProvider();
}
