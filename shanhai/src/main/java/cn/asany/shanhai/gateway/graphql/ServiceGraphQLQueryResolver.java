package cn.asany.shanhai.gateway.graphql;

import cn.asany.shanhai.gateway.bean.Consumer;
import cn.asany.shanhai.gateway.bean.Service;
import cn.asany.shanhai.gateway.graphql.types.IdType;
import cn.asany.shanhai.gateway.service.ServiceRegistryService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 服务接口
 *
 * @author limaofeng
 */
@Component
public class ServiceGraphQLQueryResolver implements GraphQLQueryResolver {

    private final ServiceRegistryService serviceRegistryService;

    public ServiceGraphQLQueryResolver(ServiceRegistryService serviceRegistryService) {
        this.serviceRegistryService = serviceRegistryService;
    }

    public Optional<Service> service(String id, IdType idType) {
        if (idType == IdType.ID) {
            return this.serviceRegistryService.getService(Long.valueOf(id));
        }
        return this.serviceRegistryService.findServiceOneByCode(id);
    }

    public List<Service> services() {
        return this.serviceRegistryService.services();
    }

    public List<Consumer> consumers() {
        return new ArrayList<>();
    }

}
