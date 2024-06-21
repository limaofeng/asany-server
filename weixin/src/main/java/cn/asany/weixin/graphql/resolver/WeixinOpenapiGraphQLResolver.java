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
package cn.asany.weixin.graphql.resolver;

import cn.asany.weixin.framework.core.Openapi;
import cn.asany.weixin.framework.exception.WeixinException;
import cn.asany.weixin.framework.oauth2.Scope;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

/**
 * 微信 Jsapi Graphql 接口
 *
 * @author limaofeng
 */
@Component
public class WeixinOpenapiGraphQLResolver implements GraphQLResolver<Openapi> {

  public String getAuthorizationUrl(Openapi openapi, String redirectUri, Scope scope, String state)
      throws WeixinException {
    return openapi.getAuthorizationUrl(redirectUri, scope, state);
  }
}
