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
  /** 必填 */
  private String required;
  /** 唯一 */
  private String unique;
  /** 多个值 */
  private String list;
  /** 默认值 */
  private String defaultValue;
  /** 数据库字段名 */
  private String databaseColumnName;
}
