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

import cn.asany.ui.resources.convert.ComponentConverter;
import cn.asany.ui.resources.domain.Component;
import cn.asany.ui.resources.graphql.input.ComponentWhereInput;
import cn.asany.ui.resources.graphql.type.ComponentConnection;
import cn.asany.ui.resources.graphql.type.ComponentCreateInput;
import cn.asany.ui.resources.graphql.type.ComponentUpdateInput;
import cn.asany.ui.resources.service.ComponentService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.LimitPageRequest;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 组件接口
 *
 * @author limaofeng
 */
@org.springframework.stereotype.Component
public class ComponentGraphqlMutationOrQueryResolver
    implements GraphQLMutationResolver, GraphQLQueryResolver {

  private final ComponentService componentService;
  private final ComponentConverter componentConverter;

  public ComponentGraphqlMutationOrQueryResolver(
      ComponentService componentService, ComponentConverter componentConverter) {
    this.componentService = componentService;
    this.componentConverter = componentConverter;
  }

  public Component createComponent(ComponentCreateInput input) {
    return componentService.save(componentConverter.toComponent(input), input.getLibraryId());
  }

  public Component updateComponent(Long id, ComponentUpdateInput input, Boolean merge) {
    return componentService.update(
        id, componentConverter.toComponent(input), input.getLibraryId(), merge);
  }

  public Boolean deleteComponent(Long id) {
    componentService.delete(id);
    return Boolean.TRUE;
  }

  public Optional<Component> component(Long id) {
    return componentService.findById(id);
  }

  public List<Component> components(
      ComponentWhereInput where, int first, int offset, Sort orderBy) {
    Pageable pageable = LimitPageRequest.of(offset, first, orderBy);
    where = ObjectUtil.defaultValue(where, new ComponentWhereInput());
    return componentService.findPage(pageable, where.toFilter()).getContent();
  }

  public ComponentConnection componentsConnection(
      ComponentWhereInput where, int page, int pageSize, Sort orderBy) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    return Kit.connection(
        componentService.findPage(pageable, where.toFilter()), ComponentConnection.class);
  }
}
