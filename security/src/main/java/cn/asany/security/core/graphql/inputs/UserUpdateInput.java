package cn.asany.security.core.graphql.inputs;

import cn.asany.storage.api.FileObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateInput {
  private String nickName;
  private FileObject avatar;
}
