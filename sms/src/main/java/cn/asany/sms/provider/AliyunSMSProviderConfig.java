package cn.asany.sms.provider;

import cn.asany.base.sms.SMSProvider;
import cn.asany.base.sms.SMSProviderConfig;
import lombok.Builder;
import lombok.Data;

/**
 * 阿里云短信服务提供商配置
 *
 * @author limaofeng
 */
@Data
@Builder
public class AliyunSMSProviderConfig implements SMSProviderConfig {
  private String key;
  private String accessKeyId;
  private String accessKeySecret;

  @Override
  public SMSProvider getProvider() {
    return SMSProvider.ALIYUN;
  }
}
