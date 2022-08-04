package cn.asany.pm.issue.core.graphql.type;

import cn.asany.pm.issue.core.domain.Issue;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

/**
 * 问题分页
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class IssueConnection extends BaseConnection<IssueConnection.IssueTaskEdge, Issue> {
  private List<IssueTaskEdge> edges;

  @Data
  public static class IssueTaskEdge implements Edge<Issue> {
    private String cursor;
    private Issue node;
  }
}
