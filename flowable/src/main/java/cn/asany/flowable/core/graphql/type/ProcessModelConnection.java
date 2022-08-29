package cn.asany.flowable.core.graphql.type;

import cn.asany.flowable.core.domain.ProcessModel;
import java.util.List;
import lombok.*;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProcessModelConnection
    extends BaseConnection<ProcessModelConnection.ProcessModelEdge, ProcessModel> {

  private List<ProcessModelEdge> edges;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ProcessModelEdge implements Edge<ProcessModel> {
    private String cursor;
    private ProcessModel node;
  }
}
