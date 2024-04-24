package cn.asany.security.auth.error;

import net.asany.jfantasy.framework.security.AuthenticationException;

/**
 * 未授权异常
 *
 * @author limaofeng
 */
public class UnauthorizedException extends AuthenticationException {

  public UnauthorizedException(String message) {
    super(message);
  }
}
