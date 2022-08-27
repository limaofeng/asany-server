package cn.asany.im.auth.listener;

import cn.asany.im.auth.service.AuthService;
import cn.asany.im.auth.service.vo.UserRegisterRequestBody;
import cn.asany.security.core.event.RegisterSuccessEvent;
import lombok.SneakyThrows;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.util.common.StringUtil;
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

  private final AuthService authService;

  public CreateImUserOnRegisterEventListener(AuthService authService) {
    this.authService = authService;
  }

  @SneakyThrows
  @Override
  public void onApplicationEvent(RegisterSuccessEvent event) {
    LoginUser user = event.getLoginUser();
    authService.userRegister(
        UserRegisterRequestBody.builder()
            .userID(user.getUid().toString())
            .faceURL(user.getAvatar())
            .nickname(user.getName())
            .platform(5)
            .phoneNumber(user.getPhone())
            .operationID(StringUtil.uuid())
            .build());
  }
}
