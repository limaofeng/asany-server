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
package cn.asany.security.core.event;

import net.asany.jfantasy.framework.security.LoginUser;
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
