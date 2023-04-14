package cn.asany.message.api;

import lombok.Builder;
import lombok.Data;

/**
 * 短信发送器配置
 *
 * @author limaofeng
 */
@Data
public class SMSSenderConfig implements ISenderConfig {

  @Builder.Default private String endpoint = "dysmsapi.aliyuncs.com";
  private String accessKeyId;
  private String accessKeySecret;
}
