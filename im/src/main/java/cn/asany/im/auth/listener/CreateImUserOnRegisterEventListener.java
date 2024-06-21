/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.im.auth.listener;

import cn.asany.im.auth.service.vo.UserRegisterRequestBody;
import cn.asany.im.error.OpenIMServerAPIException;
import cn.asany.im.user.service.UserService;
import cn.asany.security.core.event.RegisterSuccessEvent;
import lombok.SneakyThrows;
import net.asany.jfantasy.framework.security.LoginUser;
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
