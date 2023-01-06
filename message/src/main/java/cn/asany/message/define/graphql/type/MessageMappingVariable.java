package cn.asany.message.define.graphql.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class MessageMappingVariable {
  private String key;
  private String value;
}
