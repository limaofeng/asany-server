package cn.asany.pm.issue.priority.graphql;

import cn.asany.pm.issue.priority.graphql.connection.IssuePriorityConnection;
import cn.asany.pm.issue.priority.graphql.filter.IssuePriorityWhereInput;
import cn.asany.pm.issue.priority.service.IssuePriorityService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Component
public class PriorityGraphQLQueryResolver implements GraphQLQueryResolver {
  private final IssuePriorityService issuePriorityService;

  public PriorityGraphQLQueryResolver(IssuePriorityService issuePriorityService) {
    this.issuePriorityService = issuePriorityService;
  }

  /** 查询所有优先级 */
  public IssuePriorityConnection issuePrioritys(
    IssuePriorityWhereInput where, int page, int pageSize, Sort orderBy) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    return Kit.connection(
        issuePriorityService.findPage(pageable, where.toFilter()), IssuePriorityConnection.class);
  }
}
