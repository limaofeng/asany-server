package cn.asany.ui.resources.graphql;

import cn.asany.ui.resources.bean.Component;
import cn.asany.ui.resources.converter.ComponentConverter;
import cn.asany.ui.resources.graphql.input.ComponentFilter;
import cn.asany.ui.resources.graphql.type.ComponentConnection;
import cn.asany.ui.resources.graphql.type.ComponentCreateInput;
import cn.asany.ui.resources.graphql.type.ComponentUpdateInput;
import cn.asany.ui.resources.service.ComponentService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;

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
    return componentService.save(componentConverter.toComponent(input));
  }

  public Component updateComponent(Long id, ComponentUpdateInput input, Boolean merge) {
    return componentService.update(id, componentConverter.toComponent(input), merge);
  }

  public Boolean deleteComponent(Long id) {
    componentService.delete(id);
    return Boolean.TRUE;
  }

  public Optional<Component> component(Long id) {
    return componentService.findById(id);
  }

  public List<Component> components(
      ComponentFilter filter, int first, int offset, OrderBy orderBy) {
    Pager.PagerBuilder<Component> pagerBuilder = Pager.builder();
    Pager<Component> pager = pagerBuilder.first(first).pageSize(offset).orderBy(orderBy).build();
    filter = ObjectUtil.defaultValue(filter, new ComponentFilter());
    return componentService.findPager(pager, filter.build()).getPageItems();
  }

  public ComponentConnection componentsConnection(
      ComponentFilter filter, int page, int pageSize, OrderBy orderBy) {
    Pager<Component> pager = new Pager<>(page, pageSize, orderBy);
    filter = ObjectUtil.defaultValue(filter, new ComponentFilter());
    return Kit.connection(
        componentService.findPager(pager, filter.build()), ComponentConnection.class);
  }
}
