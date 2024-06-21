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
package cn.asany.nuwa.app.graphql.resolver;

import cn.asany.nuwa.app.domain.ClientSecret;
import cn.asany.security.oauth.service.AccessTokenService;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.Date;
import org.springframework.stereotype.Component;

/**
 * 客户端密钥
 *
 * @author limaofeng
 */
@Component
public class ClientSecretGraphQLResolver implements GraphQLResolver<ClientSecret> {

  private final AccessTokenService accessTokenService;

  public ClientSecretGraphQLResolver(AccessTokenService accessTokenService) {
    this.accessTokenService = accessTokenService;
  }

  public Date lastUseTime(ClientSecret clientSecret) {
    return accessTokenService.getLastUseTime(clientSecret.getClient(), clientSecret.getSecret());
  }
}
