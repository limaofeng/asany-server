package cn.asany.message.api;

import cn.asany.base.sms.SMSProvider;
import cn.asany.base.sms.SMSProviderConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短信发送器配置
 *
 * @author limaofeng
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SMSSenderConfig implements ISenderConfig {

  private SMSProvider provider;
  @Builder.Default private String endpoint = "dysmsapi.aliyuncs.com";
  private String accessKeyId;
  private String accessKeySecret;


}
