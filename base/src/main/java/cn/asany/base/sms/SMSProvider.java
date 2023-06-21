package cn.asany.base.sms;

/**
 * 短信服务提供商
 *
 * @author limaofeng
 */
public enum SMSProvider {
  // 阿里云
  ALIYUN("aliyun");

  private final String value;

  SMSProvider(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static SMSProvider of(String key) {
    if (SMSProvider.ALIYUN.getValue().equals(key)) {
      return ALIYUN;
    }
    return null;
  }
}
