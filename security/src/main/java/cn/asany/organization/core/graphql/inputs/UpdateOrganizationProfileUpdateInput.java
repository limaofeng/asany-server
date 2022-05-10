package cn.asany.organization.core.graphql.inputs;

import cn.asany.storage.api.FileObject;
import lombok.Data;

@Data
public class UpdateOrganizationProfileUpdateInput {
  private String name;
  private FileObject logo;
}
