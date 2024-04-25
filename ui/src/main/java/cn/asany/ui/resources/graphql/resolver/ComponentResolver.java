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

import cn.asany.ui.resources.domain.Component;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.Collections;
import java.util.List;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.framework.util.common.StringUtil;

@org.springframework.stereotype.Component
public class ComponentResolver implements GraphQLResolver<Component> {

  public Long libraryId(Component component) {
    String libraryId = component.get(Component.METADATA_LIBRARY_ID);
    if (StringUtil.isBlank(libraryId)) {
      return null;
    }
    return Long.valueOf(libraryId);
  }

  public List<String> tags(Component component) {
    return ObjectUtil.defaultValue(component.getTags(), Collections::emptyList);
  }
}
