package cn.asany.shanhai.gateway.graphql.resolvers;

import cn.asany.shanhai.gateway.domain.ModelGroupItem;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

/** @author limaofeng */
@Component
public class ModelGroupItemSchemaGraphQLResolver implements GraphQLResolver<ModelGroupItem> {

  public Long resourceId(ModelGroupItem item) {
    return item.getResourceId();
  }

  public String resourceType(ModelGroupItem item) {
    return item.getResourceType();
  }
}
