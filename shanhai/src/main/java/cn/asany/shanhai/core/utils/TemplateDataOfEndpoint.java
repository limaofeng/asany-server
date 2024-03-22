package cn.asany.shanhai.core.utils;

import cn.asany.shanhai.core.domain.ModelEndpoint;
import cn.asany.shanhai.core.domain.ModelEndpointReturnType;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.jfantasy.framework.util.common.StringUtil;

@AllArgsConstructor
public class TemplateDataOfEndpoint {
  private ModelEndpoint endpoint;

  public String getCode() {
    return endpoint.getCode();
  }

  public String getName() {
    return endpoint.getName();
  }

  public String getDescription() {
    return endpoint.getDescription();
  }

  public Boolean getIsArg() {
    return !endpoint.getArguments().isEmpty();
  }

  public List<TemplateDataOfEndpointArgument> getArguments() {
    return endpoint.getArguments().stream()
        .map(TemplateDataOfEndpointArgument::new)
        .collect(Collectors.toList());
  }

  public String getReturnType() {
    ModelEndpointReturnType returnType = endpoint.getReturnType();
    Boolean isList = returnType.getList();
    String type = StringUtil.upperCaseFirst(returnType.getType().getCode());
    return (isList ? "[" + type + "]" : type) + (returnType.getRequired() ? "!" : "");
  }
}
