package cn.asany.security.auth.event;

import org.jfantasy.framework.security.authentication.Authentication;
import org.jfantasy.framework.security.authentication.event.AbstractAuthenticationEvent;

/**
 * 退出成功
 *
 * @author limaofeng
 */
public class LogoutSuccessEvent extends AbstractAuthenticationEvent {

  public LogoutSuccessEvent(Authentication authentication) {
    super(authentication);
  }
}
