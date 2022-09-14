package cn.asany.shanhai.core.graphql.inputs;

import java.util.Set;
import lombok.Data;

@Data
public class ModelUpdateInput {
  /** 编码 */
  private String code;
  /** 名称 */
  private String name;
  /** 描述 */
  private String description;
  /** 字段 */
  private Set<ModelFieldInput> fields;
  /** 元数据 */
  private ModelMetadataInput metadata;
  /** 特征 */
  private Set<String> features;
}
