package cn.asany.security.auth.graphql.directive;

import cn.asany.security.core.domain.ResourceType;
import cn.asany.security.core.service.ResourceTypeService;
import graphql.schema.GraphQLDirective;
import graphql.schema.GraphQLObjectType;
import graphql.schema.idl.SchemaDirectiveWiring;
import graphql.schema.idl.SchemaDirectiveWiringEnvironment;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ResourceDirective implements SchemaDirectiveWiring {

  private final ResourceTypeService resourceTypeService;

  public ResourceDirective(ResourceTypeService resourceTypeService) {
    this.resourceTypeService = resourceTypeService;
  }

  @Override
  public GraphQLObjectType onObject(SchemaDirectiveWiringEnvironment<GraphQLObjectType> env) {
    GraphQLObjectType objectType = env.getElement();
    GraphQLDirective directive = env.getDirective();
    String name = DirectiveUtils.getArgumentValue(directive, "name");
    String label = DirectiveUtils.getArgumentValue(directive, "label");
    String description = DirectiveUtils.getArgumentValue(directive, "description");
    List<String> sourceTypes =
        DirectiveUtils.getArgumentValue(directive, "sourceTypes", new ArrayList<>());
    List<String> conditions = DirectiveUtils.getArgumentValue(directive, "conditions");

    ResourceType resourceType =
        ResourceType.builder()
            .name(name)
            .description(description)
            .resourceName(objectType.getName())
            .label(label)
            .arns(new HashSet<>(sourceTypes))
            .build();

    resourceTypeService.saveOrUpdate(resourceType);

    return SchemaDirectiveWiring.super.onObject(env);
  }
}
