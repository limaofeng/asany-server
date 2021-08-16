package cn.asany.security.core.graphql.types;

import cn.asany.base.common.SecurityType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SecurityScopeData {
  private String id;
  private SecurityType type;
  private Object value;
}
