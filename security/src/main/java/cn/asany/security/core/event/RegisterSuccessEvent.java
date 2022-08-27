package cn.asany.security.core.event;

import org.jfantasy.framework.security.LoginUser;
import org.springframework.context.ApplicationEvent;

/**
 * 登录成功
 *
 * @author limaofeng
 */
public class RegisterSuccessEvent extends ApplicationEvent {

  public RegisterSuccessEvent(LoginUser user) {
    super(user);
  }

  public LoginUser getLoginUser() {
    return (LoginUser) this.getSource();
  }
}
