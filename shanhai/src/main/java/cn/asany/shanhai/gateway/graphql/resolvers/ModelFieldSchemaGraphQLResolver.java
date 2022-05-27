package cn.asany.shanhai.gateway.graphql.resolvers;

import cn.asany.shanhai.core.domain.ModelField;
import cn.asany.shanhai.core.service.ModelService;
import cn.asany.shanhai.gateway.service.ModelGroupService;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

/** @author limaofeng */
@Component
public class ModelFieldSchemaGraphQLResolver implements GraphQLResolver<ModelField> {

  private final ModelService modelService;
  private final ModelGroupService modelGroupService;

  public ModelFieldSchemaGraphQLResolver(
      ModelService modelService, ModelGroupService modelGroupService) {
    this.modelService = modelService;
    this.modelGroupService = modelGroupService;
  }
}
