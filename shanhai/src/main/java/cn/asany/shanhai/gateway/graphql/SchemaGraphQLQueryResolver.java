package cn.asany.shanhai.gateway.graphql;

import cn.asany.shanhai.gateway.graphql.types.GraphQLSchema;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

@Component
public class SchemaGraphQLQueryResolver implements GraphQLQueryResolver {

  public GraphQLSchema schema(String id) {
    return GraphQLSchema.builder().id(id).build();
  }
}
