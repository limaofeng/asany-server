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
package cn.asany.security.oauth.graphql;

import cn.asany.security.oauth.service.AccessTokenService;
import cn.asany.security.oauth.service.TokenServiceUtils;
import cn.asany.security.oauth.vo.PersonalAccessToken;
import cn.asany.security.oauth.vo.SessionAccessToken;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import net.asany.jfantasy.framework.security.LoginUser;
import net.asany.jfantasy.framework.security.SecurityContextHolder;
import net.asany.jfantasy.framework.security.SpringSecurityUtils;
import net.asany.jfantasy.framework.security.auth.oauth2.JwtTokenPayload;
import net.asany.jfantasy.framework.security.auth.oauth2.jwt.JwtUtils;
import net.asany.jfantasy.framework.security.auth.oauth2.server.authentication.BearerTokenAuthentication;
import org.springframework.stereotype.Component;

/**
 * 访问令牌 GraphQLMutationResolver
 *
 * @author limaofeng
 */
@Component
public class AccessTokenGraphQLRootResolver
    implements GraphQLMutationResolver, GraphQLQueryResolver {

  private final AccessTokenService accessTokenService;
  private final TokenServiceUtils tokenServiceUtils;

  public AccessTokenGraphQLRootResolver(
      AccessTokenService accessTokenService, TokenServiceUtils tokenServiceUtils) {
    this.accessTokenService = accessTokenService;
    this.tokenServiceUtils = tokenServiceUtils;
  }

  public PersonalAccessToken createPersonalAccessToken(String clientId, String name) {
    return this.tokenServiceUtils.createPersonalAccessToken(clientId, name);
  }

  public boolean revokeSession(Long id) {
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    return this.tokenServiceUtils.revokeSession(user.getUid(), id);
  }

  public boolean revokePersonalAccessToken(Long id) {
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    return this.tokenServiceUtils.revokePersonalAccessToken(user.getUid(), id);
  }

  public boolean revokeToken(String token) {
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    return tokenServiceUtils.revokeToken(user.getUid(), token);
  }

  public SessionAccessToken session(Long id) {
    return this.accessTokenService.getSessionById(id);
  }

  public List<SessionAccessToken> sessions() {
    BearerTokenAuthentication authentication =
        (BearerTokenAuthentication) SecurityContextHolder.getContext().getAuthentication();
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    JwtTokenPayload payload = JwtUtils.payload(authentication.getToken().getTokenValue());
    Long uid = user.getUid();
    return this.accessTokenService.getSessions(payload.getClientId(), uid);
  }

  public List<PersonalAccessToken> personalAccessTokens() {
    BearerTokenAuthentication authentication =
        (BearerTokenAuthentication) SecurityContextHolder.getContext().getAuthentication();
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    JwtTokenPayload payload = JwtUtils.payload(authentication.getToken().getTokenValue());
    return this.accessTokenService.getPersonalAccessTokens(payload.getClientId(), user.getUid());
  }
}
