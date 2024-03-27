package cn.asany.crm.core.graphql.input;

import cn.asany.base.common.graphql.input.ContactInformationInput;
import lombok.Data;

@Data
public class CustomerUpdateInput {
  private String name;
  private ContactInformationInput contactInfo;
}
