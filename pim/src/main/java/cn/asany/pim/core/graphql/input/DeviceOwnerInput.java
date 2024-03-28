package cn.asany.pim.core.graphql.input;

import cn.asany.pim.core.domain.enums.DeviceOwnerType;
import lombok.Data;

@Data
public class DeviceOwnerInput {
  private String id;
  private DeviceOwnerType type;
  private String name;
}
