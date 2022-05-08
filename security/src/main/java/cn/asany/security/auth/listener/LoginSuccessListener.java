package cn.asany.security.auth.listener;

import cn.asany.security.auth.event.LoginSuccessEvent;
import cn.asany.security.core.service.UserService;
import org.jfantasy.framework.security.authentication.Authentication;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 登录失败
 *
 * @author limaofeng
 */
@Component
public class LoginSuccessListener implements ApplicationListener<LoginSuccessEvent> {

  private final UserService userService;

  public LoginSuccessListener(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void onApplicationEvent(LoginSuccessEvent event) {
    Authentication authentication = event.getAuthentication();

    userService.loginSuccess();

    //        AuthenticationDetails details = (AuthenticationDetails) authentication.getDetails();
    //        LoginUser user = (LoginUser) authentication.getPrincipal();
    //        if (details.getLoginType() != LoginType.password) {
    //            return;
    //        }
    //        LoginOptions options = details.getOptions();
    //        if (options.getConnected()) {
    //            this.employeeService.connected(user.get(DataConstant.FIELD_EMPLOYEE),
    // options.getProvider(), options.getSnser());
    //        }
  }
}
