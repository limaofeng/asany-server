package cn.asany.crm.core.graphql.input;

import lombok.Data;

@Data
public class CustomerUpdateInput {
  private String name;
  private ContactInformationInput contactInfo;
}
