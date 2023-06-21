package cn.asany.flowable.core.graphql;

import cn.asany.flowable.core.graphql.input.TaskWhereInput;
import cn.asany.flowable.core.graphql.type.TaskConnection;
import cn.asany.flowable.core.service.TaskInfoService;
import cn.asany.flowable.engine.idm.UserUtil;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.flowable.idm.api.User;
import org.flowable.task.api.TaskInfo;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * 任务查询
 *
 * @author limaofeng
 */
@Component
public class TaskGraphQLRootResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final TaskInfoService taskInfoService;

  public TaskGraphQLRootResolver(TaskInfoService taskInfoService) {
    this.taskInfoService = taskInfoService;
  }

  public TaskInfo task(String id) {
    return taskInfoService.getTask(id);
  }

  public Boolean assigneeTask(String id, Long assignee) {
    taskInfoService.assigneeTask(id, assignee);
    return Boolean.TRUE;
  }

  public TaskConnection myTasks(TaskWhereInput where, int page, int pageSize, Sort orderBy) {
    User user = UserUtil.toUser(SpringSecurityUtils.getCurrentUser());

    return Kit.connection(
        taskInfoService.findPage(
            PageRequest.of(page - 1, pageSize, orderBy),
            where.toFilter().equal("assignee", user.getId())),
        TaskConnection.class);
  }
}
