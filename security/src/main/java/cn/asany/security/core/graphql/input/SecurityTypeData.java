package cn.asany.security.core.graphql.input;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SecurityTypeData {
  private String key;
  private String value;
}
