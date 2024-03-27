package cn.asany.crm.core.graphql.input;

import lombok.Data;

@Data
public class ContactInformationInput {
  private String name;
  private String phone;
  private String email;
}
