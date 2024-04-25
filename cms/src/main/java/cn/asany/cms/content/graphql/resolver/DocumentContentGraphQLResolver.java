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

import cn.asany.cms.content.domain.DocumentContent;
import graphql.kickstart.tools.GraphQLResolver;
import net.asany.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Component;

@Component
public class DocumentContentGraphQLResolver implements GraphQLResolver<DocumentContent> {

  public String url(DocumentContent content) {
    if (StringUtil.isNotBlank(content.getUrl())) {
      return content.getUrl();
    }
    return content.getDocument().getPath();
  }

  public Long size(DocumentContent content) {
    if (content.getSize() != null) {
      return content.getSize();
    }
    return content.getDocument().getSize();
  }
}
