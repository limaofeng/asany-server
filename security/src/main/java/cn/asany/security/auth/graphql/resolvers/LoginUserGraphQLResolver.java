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
package cn.asany.security.auth.graphql.resolvers;

import cn.asany.security.core.util.UserUtil;
import cn.asany.storage.api.FileObject;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.Set;
import java.util.stream.Collectors;
import net.asany.jfantasy.framework.security.LoginUser;
import net.asany.jfantasy.framework.security.auth.core.AbstractAuthToken;
import net.asany.jfantasy.framework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * LoginUser Resolver
 *
 * @author limaofeng
 * @version V1.0
 */
@Component
public class LoginUserGraphQLResolver implements GraphQLResolver<LoginUser> {

  public Set<String> authorities(LoginUser user) {
    return user.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toSet());
  }

  public String token(LoginUser user) {
    AbstractAuthToken accessToken = user.getAttribute("token");
    return accessToken != null ? accessToken.getTokenValue() : null;
  }

  public String account(LoginUser user) {
    return user.getUsername();
  }

  public FileObject avatar(LoginUser user) {
    return user.getAttribute(UserUtil.LOGIN_ATTRS_AVATAR);
  }
}
