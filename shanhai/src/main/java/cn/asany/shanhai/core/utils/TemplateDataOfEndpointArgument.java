package cn.asany.shanhai.core.utils;

import cn.asany.shanhai.core.domain.ModelEndpointArgument;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TemplateDataOfEndpointArgument {
  private ModelEndpointArgument argument;

  public String getName() {
    return argument.getName();
  }

  public String getDescription() {
    return argument.getDescription();
  }

  public String getType() {
    return argument.getType();
  }

  public Boolean getRequired() {
    return argument.getRequired();
  }
}
