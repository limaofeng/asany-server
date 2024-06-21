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

import cn.asany.base.common.Ownership;
import cn.asany.base.common.domain.Owner;
import cn.asany.crm.core.service.CustomerService;
import cn.asany.crm.core.service.CustomerStoreService;
import cn.asany.pim.core.domain.Device;
import cn.asany.pim.core.domain.WarrantyCard;
import cn.asany.pim.core.domain.enums.DeviceOwnerType;
import cn.asany.pim.core.domain.enums.WarrantyStatus;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.Date;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class DeviceGraphQLResolver implements GraphQLResolver<Device> {

  private final CustomerService customerService;
  private final CustomerStoreService customerStoreService;

  public DeviceGraphQLResolver(
      CustomerService customerService, CustomerStoreService customerStoreService) {
    this.customerService = customerService;
    this.customerStoreService = customerStoreService;
  }

  public Optional<Date> warrantyStartDate(Device device) {
    return device.getWarrantyCards().stream().map(WarrantyCard::getStartDate).min(Date::compareTo);
  }

  public Optional<Date> warrantyEndDate(Device device) {
    return device.getWarrantyCards().stream().map(WarrantyCard::getEndDate).max(Date::compareTo);
  }

  public WarrantyStatus warrantyStatus(Device device) {
    return device.getWarrantyCards().stream()
        .map(WarrantyCard::getStatus)
        .max(Enum::compareTo)
        .orElse(WarrantyStatus.INACTIVE);
  }

  public Optional<Ownership> owner(Device device) {
    Owner<DeviceOwnerType> deviceOwner = device.getOwner();
    if (deviceOwner == null) {
      return Optional.empty();
    }
    if (deviceOwner.getType() == DeviceOwnerType.CUSTOMER) {
      return customerService
          .findById(Long.parseLong(deviceOwner.getId()))
          .map((customer) -> customer);
    } else if (deviceOwner.getType() == DeviceOwnerType.CUSTOMER_STORE) {
      return customerStoreService
          .findById(Long.parseLong(deviceOwner.getId()))
          .map((store) -> store);
    }
    throw new IllegalArgumentException("未知的设备所有者类型");
  }
}
