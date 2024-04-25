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
package cn.asany.cms.content.graphql.resolver;

import cn.asany.cms.content.domain.VideoContent;
import cn.asany.storage.dto.SimpleFileObject;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import net.asany.jfantasy.framework.util.common.StringUtil;
import net.asany.jfantasy.framework.util.web.WebUtil;
import net.asany.jfantasy.graphql.security.context.AuthGraphQLServletContext;
import org.springframework.stereotype.Component;

@Component
public class VideoContentGraphQLResolver implements GraphQLResolver<VideoContent> {
  public String url(VideoContent content, DataFetchingEnvironment environment) {
    String url = content.getUrl();
    if (content.getVideo() != null) {
      SimpleFileObject fileObject = ((SimpleFileObject) content.getVideo());
      url = "/preview/" + fileObject.getId();
    }
    if (StringUtil.isBlank(url)) {
      return null;
    }
    if (url.startsWith("http://") || url.startsWith("https://")) {
      return url;
    }
    AuthGraphQLServletContext context = environment.getContext();
    return WebUtil.getServerUrl(context.getRequest()) + url;
  }
}
