package cn.asany.workflow.state.graphql.connection;

import cn.asany.workflow.state.domain.State;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

/**
 * State 分页
 *
 * @author: limaofeng
 * @date: 2019/6/11 18:00
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
