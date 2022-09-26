package cn.asany.shanhai.core.graphql.inputs;

import java.util.Set;
import lombok.Data;

@Data
public class ModelCreateInput {
  /** 编码 */
  private String code;
  /** 名称 */
  private String name;
  /** 描述 */
  private String description;
  /** 字段 */
  private Set<ModelFieldInput> fields;
  /** 数据库表名 */
  private String databaseTableName;
  /** 特征 */
  private Set<String> features;
  /** 所属模块 */
  private Long module;
}
