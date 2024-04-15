package cn.asany.pim.core.graphql.input;

import lombok.Data;

@Data
public class WarrantyPolicyCreateInput {
  private Long productId;
  private String name;
  private Long duration;
}
