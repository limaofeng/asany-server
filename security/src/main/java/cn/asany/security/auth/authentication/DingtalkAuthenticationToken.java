package cn.asany.security.auth.authentication;

import lombok.Builder;
import lombok.Data;
import org.jfantasy.framework.security.authentication.SimpleAuthenticationToken;

/**
 * 钉钉认证方式
 *
 * @author limaofeng
 */
public class DingtalkAuthenticationToken
    extends SimpleAuthenticationToken<DingtalkAuthenticationToken.DingtalkCredentials> {

  public DingtalkAuthenticationToken(DingtalkCredentials credentials) {
    super(credentials);
  }

  /** 钉钉凭证 */
  @Data
  @Builder
  public static class DingtalkCredentials {
    /** 授权码 */
    private String authCode;
    /** 通过手机号绑定钉钉用户 */
    private Boolean connected;
  }
}
