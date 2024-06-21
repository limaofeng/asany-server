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
package cn.asany.cardhop.contacts.graphql.resolver;

import cn.asany.cardhop.contacts.domain.ContactGroup;
import cn.asany.cardhop.contacts.utils.IdUtils;
import graphql.kickstart.tools.GraphQLResolver;
import net.asany.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Component;

/**
 * 联系人分组
 *
 * @author limaofeng
 */
@Component
public class ContactGroupGraphQLResolver implements GraphQLResolver<ContactGroup> {
  public String id(ContactGroup group) {
    return IdUtils.toKey(group.getBook().getId(), group.getNamespace(), group.getId());
  }

  public String parentId(ContactGroup group) {
    String[] pathIds = StringUtil.tokenizeToStringArray(group.getPath(), "/");
    if (pathIds.length <= 1) {
      return null;
    }
    return IdUtils.toKey(
        group.getBook().getId(), group.getNamespace(), Long.parseLong(pathIds[pathIds.length - 2]));
  }
}
