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
package cn.asany.security.auth.listener;

import cn.asany.security.auth.event.LoginSuccessEvent;
import cn.asany.security.core.service.UserService;
import net.asany.jfantasy.framework.security.authentication.Authentication;
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

    //    LoginAuthenticationDetails details = (LoginAuthenticationDetails)
    // authentication.getDetails();
    //    LoginUser user = (LoginUser) authentication.getPrincipal();
    //    if (details.getLoginType() != LoginType.password) {
    //      return;
    //    }
    //    LoginOptions options = details.getOptions();
    //    if (options.getConnected()) {
    //      this.employeeService.connected(user.get(DataConstant.FIELD_EMPLOYEE),
    // options.getProvider(), options.getSnser());
    //    }

  }
}
