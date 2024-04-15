package cn.asany.pim.core.graphql.input;

import lombok.Data;

@Data
public class WarrantyPolicyUpdateInput {
  private String name;
  private Long duration;
}
