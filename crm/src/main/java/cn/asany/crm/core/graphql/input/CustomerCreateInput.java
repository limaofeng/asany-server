package cn.asany.crm.core.graphql.input;

import cn.asany.base.common.graphql.input.ContactInformationInput;
import lombok.Data;

@Data
public class CustomerCreateInput {
  private String name;
  private ContactInformationInput contactInfo;
}
