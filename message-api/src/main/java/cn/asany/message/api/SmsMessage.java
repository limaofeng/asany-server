package cn.asany.message.api;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 短信消息
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SmsMessage implements Message {
  /** 短信服务提供商 */
  private String provider;
  /** 短信模板参数 */
  private Map<String, String> templateParams;
  /** 签名名称 */
  private String signName;
  /** 模板代码 */
  private String templateCode;
  /** 手机号 */
  private String[] phones;
}
