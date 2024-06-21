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
package cn.asany.security.auth.checker;

import net.asany.jfantasy.framework.security.core.userdetails.PostUserDetailsChecker;
import net.asany.jfantasy.framework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * 多点登录验证
 *
 * @author limaofeng
 */
@Component
public class MultipointLoginChecker implements PostUserDetailsChecker {

  @Override
  public void check(UserDetails user) {
    System.out.println("...MultipointLoginChecker");
  }
}
