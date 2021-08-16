package cn.asany.security.auth.listener;

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

  @Override
  public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
    // 监听到 对应的事件
    System.out.println(event.getClass());
  }
}
