package cn.asany.pim.core.component;

import cn.asany.base.common.TicketTargetBuilder;
import cn.asany.base.common.TicketTargetType;
import cn.asany.pim.core.domain.Device;
import cn.asany.pim.core.service.DeviceService;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class DeviceTicketTargetBuilder implements TicketTargetBuilder<Device> {

  private final DeviceService deviceService;

  public DeviceTicketTargetBuilder(DeviceService deviceService) {
    this.deviceService = deviceService;
  }

  @Override
  public boolean supports(TicketTargetType type) {
    return type == TicketTargetType.DEVICE;
  }

  @Override
  public Optional<Device> build(Long id) {
    return deviceService.findById(id);
  }
}
