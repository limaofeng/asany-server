package cn.asany.shanhai.core.graphql.resolver;

import cn.asany.shanhai.core.domain.Model;
import cn.asany.shanhai.core.domain.ModelField;
import cn.asany.shanhai.view.domain.ModelView;
import cn.asany.shanhai.view.domain.enums.ModelViewType;
import cn.asany.shanhai.view.service.ModelViewService;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.stereotype.Component;

@Component
public class ModelGraphQLResolver implements GraphQLResolver<Model> {

  private final ModelViewService modelViewService;

  public ModelGraphQLResolver(ModelViewService modelViewService) {
    this.modelViewService = modelViewService;
  }

  public List<ModelField> fields(Model model) {
    return ObjectUtil.sort(new ArrayList<>(model.getFields()), "sort", "asc");
  }

  public List<ModelView> views(Model model) {
    return ObjectUtil.filter(model.getViews(), "defaultView", false);
  }

  public Optional<ModelView> defaultView(Model model, ModelViewType type) {
    return modelViewService.getDefaultView(model.getId(), type);
  }
}
