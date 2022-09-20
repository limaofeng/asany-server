package cn.asany.shanhai.core.graphql.inputs;

import cn.asany.storage.api.FileObject;
import lombok.Data;

@Data
public class ModuleUpdateInput {
  /** 编码 */
  private String code;
  /** 名称 */
  private String name;
  /** 图片 */
  private FileObject picture;
  /** 描述 */
  private String description;
}
