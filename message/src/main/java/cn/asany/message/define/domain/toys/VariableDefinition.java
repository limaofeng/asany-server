package cn.asany.message.define.domain.toys;

import lombok.Data;

/**
 * 变量定义
 *
 * @author limaofeng
 */
@Data
public class VariableDefinition {
  /** 变量名称 */
  private String name;
  /** 变量描述 */
  private String description;
  /** 变量类型 */
  private String type;
  /** 默认值 */
  private String defaultValue;
  /** 是否必填 */
  private boolean required;
  /** 示例 */
  private String example;
}
