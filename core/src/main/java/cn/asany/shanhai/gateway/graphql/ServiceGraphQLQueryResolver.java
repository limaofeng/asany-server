package cn.asany.shanhai.gateway.graphql;

import cn.asany.shanhai.gateway.bean.Consumer;
import cn.asany.shanhai.gateway.bean.Service;
import cn.asany.shanhai.gateway.service.ServiceRegistryService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ServiceGraphQLQueryResolver implements GraphQLQueryResolver {

    private ServiceRegistryService serviceRegistryService;

    public List<Service> services() {
        return this.serviceRegistryService.services();
    }

    public List<Consumer> consumers() {
        return new ArrayList<>();
    }

}
