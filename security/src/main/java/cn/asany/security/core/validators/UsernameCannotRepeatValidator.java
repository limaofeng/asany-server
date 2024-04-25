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
package cn.asany.security.core.validators;

import cn.asany.security.core.service.UserService;
import net.asany.jfantasy.framework.spring.validation.ValidationException;
import net.asany.jfantasy.framework.spring.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 用户名不能重复
 *
 * @author limaofeng
 */
@Component("user.UsernameCannotRepeatValidator")
public class UsernameCannotRepeatValidator implements Validator<String> {

  private final UserService userService;

  @Autowired
  public UsernameCannotRepeatValidator(UserService userService) {
    this.userService = userService;
  }

  @Override
  public void validate(String value) throws ValidationException {
    if (userService.findOneByUsername(value).isPresent()) {
      throw new ValidationException("用户名[" + value + "]已经存在");
    }
  }
}
