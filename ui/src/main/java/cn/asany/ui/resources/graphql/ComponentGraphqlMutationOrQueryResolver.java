package cn.asany.ui.resources.graphql;

import cn.asany.ui.resources.convert.ComponentConverter;
import cn.asany.ui.resources.domain.Component;
import cn.asany.ui.resources.graphql.input.ComponentFilter;
import cn.asany.ui.resources.graphql.type.ComponentConnection;
import cn.asany.ui.resources.graphql.type.ComponentCreateInput;
import cn.asany.ui.resources.graphql.type.ComponentUpdateInput;
import cn.asany.ui.resources.service.ComponentService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.LimitPageRequest;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;
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

  public List<Component> components(ComponentFilter filter, int first, int offset, Sort orderBy) {
    Pageable pageable = LimitPageRequest.of(offset, first, orderBy);
    filter = ObjectUtil.defaultValue(filter, new ComponentFilter());
    return componentService.findPage(pageable, filter.build()).getContent();
  }

  public ComponentConnection componentsConnection(
      ComponentFilter filter, int page, int pageSize, Sort orderBy) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    filter = ObjectUtil.defaultValue(filter, new ComponentFilter());
    return Kit.connection(
        componentService.findPage(pageable, filter.build()), ComponentConnection.class);
  }
}
