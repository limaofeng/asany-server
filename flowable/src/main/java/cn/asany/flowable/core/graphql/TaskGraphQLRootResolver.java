package cn.asany.flowable.core.graphql;

import cn.asany.flowable.core.graphql.input.TaskFilter;
import cn.asany.flowable.core.graphql.type.TaskConnection;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.stream.Collectors;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.api.TaskInfoQuery;
import org.flowable.task.api.TaskQuery;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.jfantasy.graphql.PageInfo;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class TaskGraphQLRootResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final TaskService taskService;

  public TaskGraphQLRootResolver(TaskService taskService) {
    this.taskService = taskService;
  }

  public TaskConnection myTasks(TaskFilter filter, int page, int pageSize, Sort orderBy) {
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    TaskInfoQuery<TaskQuery, Task> taskQuery =
        taskService.createTaskQuery().taskAssignee(String.valueOf(user.getUid()));

    long totalCount = taskQuery.count();

    List<? extends TaskInfo> tasks = taskQuery.listPage((page - 1) * pageSize, pageSize);

    TaskConnection connection = new TaskConnection();
    connection.setEdges(
        tasks.stream()
            .map(
                task ->
                    TaskConnection.TaskInfoEdge.builder().node(task).cursor(task.getId()).build())
            .collect(Collectors.toList()));

    PageInfo pageInfo =
        PageInfo.builder()
            .total(totalCount)
            .current(page)
            .pageSize(pageSize)
            .totalPages(
                (int)
                    (totalCount % pageSize == 0
                        ? totalCount / pageSize
                        : totalCount / pageSize + 1))
            .build();

    connection.setTotalCount((int) pageInfo.getTotal());
    connection.setTotalPage(pageInfo.getTotalPages());
    connection.setCurrentPage(pageInfo.getCurrent());
    connection.setPageSize(pageInfo.getPageSize());
    connection.setPageInfo(pageInfo);
    return connection;
  }
}
