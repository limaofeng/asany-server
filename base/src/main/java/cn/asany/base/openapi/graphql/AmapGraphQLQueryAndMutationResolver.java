package cn.asany.base.openapi.graphql;

import cn.asany.base.openapi.apis.AmapOpenAPI;
import cn.asany.base.openapi.service.OpenAPIService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

@Component
public class AmapGraphQLQueryAndMutationResolver
    implements GraphQLQueryResolver, GraphQLMutationResolver {
  private final OpenAPIService openAPIService;

  public AmapGraphQLQueryAndMutationResolver(OpenAPIService openAPIService) {
    this.openAPIService = openAPIService;
  }

  public AmapOpenAPI amapOpenAPI() {
    return openAPIService.getDefaultAmap();
  }
}
