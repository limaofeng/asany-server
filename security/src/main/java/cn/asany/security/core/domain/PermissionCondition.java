package cn.asany.security.core.domain;

import cn.asany.security.auth.graphql.directive.ConditionOperator;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermissionCondition implements Serializable {
  private String resourceType;
  private String fieldName;
  private ConditionOperator operator;
  private List<String> values;
}
