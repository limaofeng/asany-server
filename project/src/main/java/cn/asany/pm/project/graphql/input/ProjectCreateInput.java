package cn.asany.pm.project.graphql.input;

import cn.asany.storage.api.FileObject;
import lombok.Data;

@Data
public class ProjectCreateInput {
  private FileObject logo;
  private String name;
  private String description;
}
