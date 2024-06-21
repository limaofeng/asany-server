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
package cn.asany.openapi.graphql.type;

import cn.asany.openapi.apis.AmapOpenAPI;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import net.asany.jfantasy.framework.util.common.StringUtil;
import net.asany.jfantasy.framework.util.web.WebUtil;
import net.asany.jfantasy.graphql.security.context.AuthGraphQLServletContext;
import org.springframework.stereotype.Component;

/**
 * AMAP OpenAPI
 *
 * @author limaofeng
 */
@Component
public class AmapOpenAPIGraphQLResolver implements GraphQLResolver<AmapOpenAPI> {

  public AmapOpenAPI.IpResult ip(AmapOpenAPI api, String ip, DataFetchingEnvironment environment) {
    AuthGraphQLServletContext context = environment.getContext();
    if (StringUtil.isBlank(ip)) {
      ip = WebUtil.getClientIP(context.getRequest());
    }
    return api.ip(ip);
  }
}
