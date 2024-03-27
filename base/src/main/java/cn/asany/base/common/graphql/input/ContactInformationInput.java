package cn.asany.base.common.graphql.input;

import lombok.Data;

@Data
public class ContactInformationInput {
  private String name;
  private String phone;
  private String email;
  private AddressInput address;
}
