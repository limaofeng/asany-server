package cn.asany.flowable.core.graphql.input;

import cn.asany.flowable.core.graphql.type.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskFilter {
  private TaskStatus status;
}
