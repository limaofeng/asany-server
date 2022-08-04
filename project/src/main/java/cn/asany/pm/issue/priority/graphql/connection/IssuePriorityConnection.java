package cn.asany.pm.issue.priority.graphql.connection;

import cn.asany.pm.issue.priority.domain.Priority;
import java.util.List;
import lombok.*;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

/**
 * 优先级
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class IssuePriorityConnection
    extends BaseConnection<IssuePriorityConnection.IssuePriorityEdge, Priority> {
  private List<IssuePriorityEdge> edges;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class IssuePriorityEdge implements Edge<Priority> {
    private String cursor;
    private Priority node;
  }
}
