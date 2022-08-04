package cn.asany.pm.issue.priority.graphql;

import cn.asany.pm.issue.priority.domain.Priority;
import cn.asany.pm.issue.priority.service.IssuePriorityService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Component
public class PriorityGraphQLMutationResolver implements GraphQLMutationResolver {

  private final IssuePriorityService issuePriorityService;

  public PriorityGraphQLMutationResolver(IssuePriorityService issuePriorityService) {
    this.issuePriorityService = issuePriorityService;
  }

  /** 增加优先级 */
  public Priority createIssuePriority(Priority priority) {
    return issuePriorityService.save(priority);
  }

  /** 更新优先级 */
  public Priority updateIssuePriority(Long id, Boolean merge, Priority priority) {
    return issuePriorityService.updateIssuePriority(id, merge, priority);
  }

  /** 删除优先级 */
  public Boolean removeIssuePriority(Long id) {
    return issuePriorityService.removeIssuePriority(id);
  }
}
