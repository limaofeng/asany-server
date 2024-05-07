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
package cn.asany.security.core.service;

import cn.asany.security.core.domain.User;
import cn.asany.security.core.util.UserUtil;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.asany.jfantasy.framework.security.LoginUser;
import net.asany.jfantasy.framework.security.core.userdetails.UserDetailsService;
import net.asany.jfantasy.framework.security.core.userdetails.UsernameNotFoundException;
import org.dataloader.DataLoader;
import org.springframework.context.support.MessageSourceAccessor;

/**
 * 用户服务
 *
 * @author limaofeng
 */
public class DefaultUserDetailsService implements UserDetailsService<LoginUser> {

  private final UserService userService;
  protected final MessageSourceAccessor messages;
  private final DataLoader<Long, User> userDataLoader;

  public DefaultUserDetailsService(
      UserService userService,
      DataLoader<Long, User> userDataLoader,
      MessageSourceAccessor messages) {
    this.userService = userService;
    this.messages = messages;
    this.userDataLoader = userDataLoader;
  }

  @Override
  public LoginUser loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> optional = this.userService.findOneByUsername(username);

    if (optional.isEmpty()) {
      optional = userService.findOneByPhone(username);
    }

    // 用户不存在
    if (optional.isEmpty()) {
      throw new UsernameNotFoundException(
          this.messages.getMessage(
              "AbstractUserDetailsAuthenticationProvider.badCredentials", "Bad credentials"));
    }
    return UserUtil.buildLoginUser(optional.get());
  }

  @Override
  public CompletableFuture<LoginUser> loadUserById(Long id) {
    return userDataLoader.load(id).thenApply(UserUtil::buildLoginUser);
  }
}
