package cn.asany.autoconfigure.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "sms.provider")
public class SmsProviderProperties {
  private AliyunProvider aliyun;

  @Data
  public static class AliyunProvider {
    /** 密钥Key */
    private String accessKeyId;
    /** 密钥 */
    private String accessKeySecret;
  }
}
