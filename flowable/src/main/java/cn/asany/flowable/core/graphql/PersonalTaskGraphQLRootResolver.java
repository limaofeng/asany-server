package cn.asany.flowable.core.graphql;

import cn.asany.flowable.core.domain.PersonalTask;
import cn.asany.flowable.core.service.TaskInfoService;
import cn.asany.flowable.engine.idm.UserUtil;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.springframework.stereotype.Component;

/**
 * 任务查询
 *
 * @author limaofeng
 */
@Component
public class PersonalTaskGraphQLRootResolver
    implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final TaskInfoService taskInfoService;

  public PersonalTaskGraphQLRootResolver(TaskInfoService taskInfoService) {
    this.taskInfoService = taskInfoService;
  }

  public Boolean createPersonalTask(PersonalTask input) {
    taskInfoService.createPersonalTask(
        input, UserUtil.toUser(SpringSecurityUtils.getCurrentUser()));
    return Boolean.TRUE;
  }
}
