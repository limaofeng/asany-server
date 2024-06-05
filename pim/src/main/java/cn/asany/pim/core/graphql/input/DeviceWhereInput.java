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
package cn.asany.pim.core.graphql.input;

import cn.asany.crm.core.domain.CustomerStore;
import cn.asany.crm.core.service.CustomerStoreService;
import cn.asany.pim.core.domain.Device;
import cn.asany.pim.core.domain.enums.DeviceOwnerType;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.spring.SpringBeanUtils;
import net.asany.jfantasy.graphql.inputs.WhereInput;
import org.springframework.data.domain.Sort;

public class DeviceWhereInput extends WhereInput<DeviceWhereInput, Device> {

  public void setCustomer(Long id) {
    CustomerStoreService storeService = SpringBeanUtils.getBean(CustomerStoreService.class);
    List<CustomerStore> stores =
        storeService.findAll(
            PropertyFilter.newFilter().equal("customer.id", id), 0, 1000, Sort.unsorted());
    this.filter.and(
        PropertyFilter.newFilter()
            .equal("owner.type", DeviceOwnerType.CUSTOMER_STORE)
            .in(
                "owner.id",
                stores.stream()
                    .map(item -> item.getId().toString())
                    .toList()
                    .toArray(new String[0])));
  }

  public void setCustomerStore(String id) {
    this.filter.equal("owner.type", DeviceOwnerType.CUSTOMER_STORE).equal("owner.id", id);
  }
}
