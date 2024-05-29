package cn.asany.pim.core.graphql.resolver;

import cn.asany.crm.core.domain.CustomerStore;
import cn.asany.pim.core.domain.enums.DeviceOwnerType;
import cn.asany.pim.core.service.DeviceService;
import graphql.kickstart.tools.GraphQLResolver;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.stereotype.Component;

@Component
public class CustomerStoreGraphQLResolver implements GraphQLResolver<CustomerStore> {

  private final DeviceService deviceService;

  public CustomerStoreGraphQLResolver(DeviceService deviceService) {
    this.deviceService = deviceService;
  }

  public long deviceCount(CustomerStore store) {
    return deviceService.count(PropertyFilter.newFilter()
        .equal("owner.type", DeviceOwnerType.CUSTOMER_STORE)
      .equal("owner.id", store.getId().toString()));
  }

}
