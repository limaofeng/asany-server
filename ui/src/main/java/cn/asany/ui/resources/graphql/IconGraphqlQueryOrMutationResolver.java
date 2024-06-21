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
package cn.asany.ui.resources.graphql;

import cn.asany.ui.resources.convert.IconConverter;
import cn.asany.ui.resources.domain.Icon;
import cn.asany.ui.resources.graphql.input.IconCreateInput;
import cn.asany.ui.resources.graphql.input.IconUpdateInput;
import cn.asany.ui.resources.graphql.input.IconWhereInput;
import cn.asany.ui.resources.service.IconService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.Set;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.stereotype.Component;

/**
 * 图标 接口
 *
 * @author limaofeng
 */
@Component
public class IconGraphqlQueryOrMutationResolver
    implements GraphQLMutationResolver, GraphQLQueryResolver {

  private final IconService iconService;
  private final IconConverter iconConverter;

  public IconGraphqlQueryOrMutationResolver(IconService iconService, IconConverter iconConverter) {
    this.iconService = iconService;
    this.iconConverter = iconConverter;
  }

  public Set<Icon> icons(IconWhereInput where) {
    where = ObjectUtil.defaultValue(where, IconWhereInput::new);
    PropertyFilter propertyFilter = where.toFilter();
    // TODO: 需要替换新的写法
    //        if (filter.getLibrary_in() != null) {
    //            builder.and(new LibrarySpecification(filter.getLibrary_in()));
    //        } else if (filter.getLibrary() != null) {
    //            builder.and(new LibrarySpecification(filter.getLibrary()));
    //        }
    return this.iconService.findAll(propertyFilter);
  }

  public Icon createIcon(IconCreateInput input) {
    Long libraryId = input.getLibraryId();
    Icon icon = iconConverter.toIcon(input);
    return iconService.save(libraryId, icon);
  }

  public Icon updateIcon(Long id, IconUpdateInput input) {
    Long libraryId = input.getLibraryId();
    Icon icon = iconConverter.toIcon(input);
    icon.setId(id);
    return iconService.save(libraryId, icon);
  }

  public Boolean deleteIcon(Long id) {
    iconService.delete(id);
    return Boolean.TRUE;
  }
}
