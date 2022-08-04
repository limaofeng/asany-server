package cn.asany.pm.issue.core.graphql.resolver;

import cn.asany.pm.issue.core.graphql.type.IssueOperation;
import cn.asany.pm.issue.core.service.IssueService;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

/**
 * 问题操作
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
@Component
public class IssueOperationGraphQLResolver implements GraphQLResolver<IssueOperation> {

  private final IssueService issueService;

  public IssueOperationGraphQLResolver(IssueService issueService) {
    this.issueService = issueService;
  }

  public Boolean grants(IssueOperation operation) {
    if (operation != null) {
      // 通过操作的id，查询权限
      return issueService.getGrants(operation);
    } else {
      return false;
    }
  }
}
