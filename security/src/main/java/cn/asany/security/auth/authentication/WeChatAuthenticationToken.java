package cn.asany.security.auth.authentication;

import lombok.Builder;
import lombok.Data;
import org.jfantasy.framework.security.authentication.SimpleAuthenticationToken;

/**
 * 微信认证凭证
 *
 * @author limaofeng
 */
public class WeChatAuthenticationToken
    extends SimpleAuthenticationToken<WeChatAuthenticationToken.WeChatCredentials> {

  public WeChatAuthenticationToken(WeChatCredentials credentials) {
    super(credentials);
  }

  /** 微信凭证 */
  @Data
  @Builder
  public static class WeChatCredentials {

    /** 授权码 */
    private String authCode;
    /** 通过手机号绑定钉钉用户 */
    private Boolean connected;
  }
}
