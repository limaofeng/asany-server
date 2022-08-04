package cn.asany.pm.rule.graphql.input;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Data
public class IssueIntegralRuleFilter {
  @JsonProperty("code_eq")
  private String codeEq;
}
