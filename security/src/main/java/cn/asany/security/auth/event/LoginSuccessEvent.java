package cn.asany.security.auth.event;

import org.jfantasy.framework.security.authentication.Authentication;
import org.jfantasy.framework.security.authentication.event.AbstractAuthenticationEvent;

/**
 * 登录成功
 *
 * @author limaofeng
 */
public class LoginSuccessEvent extends AbstractAuthenticationEvent {

  public LoginSuccessEvent(Authentication authentication) {
    super(authentication);
  }
}
