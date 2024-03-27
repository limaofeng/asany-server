package cn.asany.crm.core.graphql.input;

import lombok.Data;

@Data
public class CustomerCreateInput {
  private String name;
  private ContactInformationInput contactInfo;
}
