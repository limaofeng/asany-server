package cn.asany.workflow.state.graphql.connection;

import cn.asany.workflow.state.domain.State;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.asany.jfantasy.graphql.Edge;
import net.asany.jfantasy.graphql.types.BaseConnection;

/**
 * State 分页
 *
 * @author: limaofeng
 * @date 2022/7/28 9:12 9:12
 */
@Data
public class StateConnection extends BaseConnection<StateConnection.StateEdge, State> {
  private List<StateEdge> edges;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class StateEdge implements Edge<State> {
    private String cursor;
    private State node;
  }
}
