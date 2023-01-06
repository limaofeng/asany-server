package cn.asany.shanhai.core.graphql.resolver;

import cn.asany.shanhai.core.domain.ModelEndpoint;
import cn.asany.shanhai.core.domain.ModelEndpointArgument;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.ArrayList;
import java.util.List;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.stereotype.Component;

@Component
public class ModelEndpointGraphQLResolver implements GraphQLResolver<ModelEndpoint> {

  public List<ModelEndpointArgument> arguments(ModelEndpoint endpoint) {
    return ObjectUtil.sort(new ArrayList<>(endpoint.getArguments()), "index", "asc");
  }
}
