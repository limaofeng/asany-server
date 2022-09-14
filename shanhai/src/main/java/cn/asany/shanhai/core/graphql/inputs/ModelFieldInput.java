package cn.asany.shanhai.core.graphql.inputs;

import lombok.Data;

@Data
public class ModelFieldInput {
  /** 编码 */
  private String code;
  /** 名称 */
  private String name;
  /** 类型 */
  private String type;
}
