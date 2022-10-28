package cn.asany.shanhai.view.graphql;

import cn.asany.shanhai.view.domain.ModelView;
import cn.asany.shanhai.view.graphql.input.ModelViewFilter;
import cn.asany.shanhai.view.service.ModelViewService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class ModelViewGraphQLRootResolver implements GraphQLMutationResolver, GraphQLQueryResolver {

  private final ModelViewService modelViewService;

  public ModelViewGraphQLRootResolver(ModelViewService modelViewService) {
    this.modelViewService = modelViewService;
  }

  public Optional<ModelView> modelView(Long id) {
    return this.modelViewService.findById(id);
  }

  public List<ModelView> modelViews(
      Long id, ModelViewFilter filter, int offset, int limit, Sort orderBy) {
    return this.modelViewService.findAll(
        filter.getBuilder().equal("model.module.id", id).build(), offset, limit, orderBy);
  }
}
