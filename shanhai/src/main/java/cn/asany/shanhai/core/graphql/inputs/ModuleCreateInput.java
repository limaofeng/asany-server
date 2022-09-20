package cn.asany.shanhai.core.graphql.inputs;

import lombok.Data;

@Data
public class ModuleCreateInput {
  /** 编码 */
  private String code;
  /** 名称 */
  private String name;
  /** 描述 */
  private String description;
}
