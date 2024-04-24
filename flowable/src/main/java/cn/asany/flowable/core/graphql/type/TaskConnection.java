package cn.asany.flowable.core.graphql.type;

import java.util.List;
import lombok.*;
import org.flowable.task.api.TaskInfo;
import net.asany.jfantasy.graphql.Edge;
import net.asany.jfantasy.graphql.types.BaseConnection;

/**
 * 任务连接
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TaskConnection extends BaseConnection<TaskConnection.TaskInfoEdge, TaskInfo> {

  private List<TaskInfoEdge> edges;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class TaskInfoEdge implements Edge<TaskInfo> {
    private String cursor;
    private TaskInfo node;
  }
}
