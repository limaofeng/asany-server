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
package cn.asany.ui.resources.graphql.resolver;

import cn.asany.ui.resources.domain.Icon;
import graphql.kickstart.tools.GraphQLResolver;
import net.asany.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Component;

@Component
public class IconResolver implements GraphQLResolver<Icon> {

  public Long libraryId(Icon icon) {
    String libraryId = icon.get(Icon.METADATA_LIBRARY_ID);
    if (StringUtil.isBlank(libraryId)) {
      return null;
    }
    return Long.valueOf(libraryId);
  }
}
