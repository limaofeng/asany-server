package cn.asany.security.auth.authentication;

import cn.asany.security.auth.graphql.types.LoginOptions;
import cn.asany.security.auth.graphql.types.LoginType;
import jakarta.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;

/**
 * 登录详情
 *
 * @author limaofeng
 */
@Getter
public class LoginAuthenticationDetails {
  private final LoginType loginType;
  private final LoginOptions options;
  private HttpServletRequest request;

  public LoginAuthenticationDetails(LoginType loginType, LoginOptions options) {
    this.loginType = loginType;
    this.options = options;
  }

  public LoginAuthenticationDetails(
      LoginType loginType, LoginOptions options, HttpServletRequest request) {
    this.loginType = loginType;
    this.options = options;
    this.request = request;
  }
}
