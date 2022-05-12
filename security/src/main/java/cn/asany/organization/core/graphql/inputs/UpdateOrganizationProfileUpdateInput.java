package cn.asany.organization.core.graphql.inputs;

import cn.asany.base.common.graphql.input.AddressInput;
import cn.asany.storage.api.FileObject;
import lombok.Data;

@Data
public class UpdateOrganizationProfileUpdateInput {
  private String name;
  private String description;
  private FileObject logo;
  private String email;
  private String url;
  private AddressInput location;
}
