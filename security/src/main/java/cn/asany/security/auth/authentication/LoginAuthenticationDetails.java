package cn.asany.security.auth.authentication;

import cn.asany.security.auth.graphql.types.LoginOptions;
import cn.asany.security.auth.graphql.types.LoginType;

/**
 * 登录详情
 *
 * @author limaofeng
 */
public class LoginAuthenticationDetails {
  private LoginType loginType;
  private LoginOptions options;

  public LoginAuthenticationDetails(LoginType loginType, LoginOptions options) {
    this.loginType = loginType;
    this.options = options;
  }

  public LoginType getLoginType() {
    return loginType;
  }

  public LoginOptions getOptions() {
    return options;
  }
}
