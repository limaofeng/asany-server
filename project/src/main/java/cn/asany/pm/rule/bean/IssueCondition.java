package cn.asany.pm.rule.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IssueCondition {
  /** 条件名称 */
  private String name;
  /** 对应ID */
  private Long id;
}
