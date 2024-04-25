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
package cn.asany.im.user.graphql.resolvers;

import cn.asany.im.auth.graphql.type.OnlineStatus;
import cn.asany.im.auth.graphql.type.OnlineStatusDetails;
import cn.asany.im.auth.graphql.type.Platform;
import cn.asany.im.auth.service.AuthService;
import cn.asany.im.error.OpenIMServerAPIException;
import cn.asany.im.user.service.UserService;
import cn.asany.im.utils.OpenIMUtils;
import cn.asany.security.core.domain.User;
import graphql.kickstart.tools.GraphQLResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("im.user.UserGraphQLResolver")
public class UserGraphQLResolver implements GraphQLResolver<User> {

  private final AuthService authService;
  private final UserService userService;

  public UserGraphQLResolver(AuthService authService, UserService userService) {
    this.authService = authService;
    this.userService = userService;
  }

  public String imToken(User user, Platform platform) throws OpenIMServerAPIException {
    try {
      return this.authService.token(platform, String.valueOf(user.getId()));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return "";
    }
  }

  public OnlineStatusDetails onlineStatus(User user) throws OpenIMServerAPIException {
    try {
      return OpenIMUtils.onlineStatus(authService, userService, user.getId());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return OnlineStatusDetails.builder().status(OnlineStatus.offline).build();
    }
  }
}
