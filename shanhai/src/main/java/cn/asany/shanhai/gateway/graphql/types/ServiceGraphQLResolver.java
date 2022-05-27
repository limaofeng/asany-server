package cn.asany.shanhai.gateway.graphql.types;

import cn.asany.shanhai.gateway.domain.Service;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

@Component
public class ServiceGraphQLResolver implements GraphQLResolver<Service> {

  public String protocol(Service service) {
    return service.getProtocol().toString();
  }
}
