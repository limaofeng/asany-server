package cn.asany.storage.data.graphql;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

@Component
public class DownloadGraphQLResolver implements GraphQLMutationResolver, GraphQLQueryResolver {

  public String createDownloadURL() {
    return "";
  }
}
