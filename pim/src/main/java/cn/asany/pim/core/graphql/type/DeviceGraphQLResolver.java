package cn.asany.pim.core.graphql.type;

import cn.asany.base.common.Ownership;
import cn.asany.base.common.domain.Owner;
import cn.asany.crm.core.domain.Customer;
import cn.asany.crm.core.domain.CustomerStore;
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

  public Ownership owner(Device device) {
    Owner<DeviceOwnerType> deviceOwner = device.getOwner();
    if (deviceOwner == null) {
      return null;
    }
    if (deviceOwner.getType() == DeviceOwnerType.CUSTOMER) {
      return Customer.builder()
          .id(Long.parseLong(deviceOwner.getId()))
          .name(deviceOwner.getName())
          .build();
    } else if (deviceOwner.getType() == DeviceOwnerType.CUSTOMER_STORE) {
      return CustomerStore.builder()
          .id(Long.parseLong(deviceOwner.getId()))
          .name(deviceOwner.getName())
          .build();
    }
    return null;
  }
}
