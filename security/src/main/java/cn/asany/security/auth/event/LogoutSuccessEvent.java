package cn.asany.security.auth.event;

import org.jfantasy.framework.security.authentication.Authentication;
import org.jfantasy.framework.security.authentication.event.AbstractAuthenticationEvent;

/**
 * ιεΊζε
 *
 * @author limaofeng
 */
public class LogoutSuccessEvent extends AbstractAuthenticationEvent {

  public LogoutSuccessEvent(Authentication authentication) {
    super(authentication);
  }
}
