package cn.asany.pm.rule.graphql.connection;

import cn.asany.pm.rule.bean.IssueIntegralRule;
import java.util.List;
import lombok.*;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

/**
 * Issue Integral Rule Connection
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class IssueIntegralRuleConnection
    extends BaseConnection<IssueIntegralRuleConnection.IssueIntegralRuleEdge, IssueIntegralRule> {
  private List<IssueIntegralRuleConnection.IssueIntegralRuleEdge> edges;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class IssueIntegralRuleEdge implements Edge<IssueIntegralRule> {
    private String cursor;
    private IssueIntegralRule node;
  }
}
