package cn.asany.security.auth.listener;

import cn.asany.security.core.service.UserService;
import org.jfantasy.framework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 登录失败
 *
 * @author limaofeng
 */
@Component
public class LoginFailureListener
    implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

  private final UserService userService;

  public LoginFailureListener(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
    // 监听到 对应的事件
    this.userService.loginFailure();
  }
}
