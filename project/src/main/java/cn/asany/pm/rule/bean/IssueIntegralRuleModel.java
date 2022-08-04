package cn.asany.pm.rule.bean;

import java.util.List;
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
public class IssueIntegralRuleModel {
  private String name;
  private List<IssueCondition> issueConditions;
  private IssueIntegralRuleEnum code;
  private String remarks;
}
