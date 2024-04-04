package cn.asany.base;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModuleConfig<T extends IModuleProperties> {

  private String tenant;
  private Long defaultOrganization;
  private T properties;
}
