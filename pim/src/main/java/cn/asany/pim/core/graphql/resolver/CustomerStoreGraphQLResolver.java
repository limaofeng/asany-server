/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
    return deviceService.count(
        PropertyFilter.newFilter()
            .equal("owner.type", DeviceOwnerType.CUSTOMER_STORE)
            .equal("owner.id", store.getId().toString()));
  }
}
