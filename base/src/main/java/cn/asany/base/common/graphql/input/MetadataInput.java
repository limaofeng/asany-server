package cn.asany.base.common.graphql.input;

import cn.asany.storage.api.FileObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetadataInput {
  private FileObject thumb;
  private String title;
  private String description;
}
