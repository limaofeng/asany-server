package cn.asany.openapi.configs;

import cn.asany.openapi.IOpenApiConfig;
import cn.asany.weixin.framework.session.WeixinAppType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 微信配置
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeixinConfig implements IOpenApiConfig {
  /** App ID */
  private String appId;
  /** 密钥 */
  private String appSecret;
  /** 公众号类型 */
  private WeixinAppType type;
  /** 原始ID */
  private String primitiveId;
  /** 微信服务器配置的token */
  private String token;
  /** 微信生成的 ASEKey */
  private String aesKey;
  /**
   * 代理ID<br>
   * 企业号才需要配置该属性
   */
  private Integer agentId;
}
