package cn.asany.im.auth.listener;

import cn.asany.im.auth.service.vo.UserRegisterRequestBody;
import cn.asany.im.error.OpenIMServerAPIException;
import cn.asany.im.user.service.UserService;
import cn.asany.security.core.event.RegisterSuccessEvent;
import lombok.SneakyThrows;
import org.jfantasy.framework.security.LoginUser;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 登录失败
 *
 * @author limaofeng
 */
@Component
public class CreateImUserOnRegisterEventListener
    implements ApplicationListener<RegisterSuccessEvent> {

  private final UserService userService;

  public CreateImUserOnRegisterEventListener(UserService userService) {
    this.userService = userService;
  }

  @SneakyThrows(OpenIMServerAPIException.class)
  @Override
  public void onApplicationEvent(RegisterSuccessEvent event) {
    LoginUser user = event.getLoginUser();
    userService.userRegister(
        UserRegisterRequestBody.builder()
            .addUser(user.getUid().toString(), user.getName(), user.getAvatar())
            .build());
  }
}
