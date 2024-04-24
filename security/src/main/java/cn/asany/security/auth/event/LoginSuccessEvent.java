package cn.asany.security.auth.event;

import net.asany.jfantasy.framework.security.authentication.Authentication;
import net.asany.jfantasy.framework.security.authentication.event.AbstractAuthenticationEvent;

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
