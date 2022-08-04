package cn.asany.pm.rule.graphql.resolver;

import cn.asany.pm.rule.bean.IssueAllocationRule;
import cn.asany.pm.rule.bean.IssueCondition;
import cn.asany.pm.rule.service.IssueAllocationRuleService;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IssueAllocationRuleGraphQLResolver implements GraphQLResolver<IssueAllocationRule> {

  @Autowired private IssueAllocationRuleService issueAllocationRuleService;

  public IssueCondition contion(IssueAllocationRule issueAllocationRule) {
    return issueAllocationRuleService.getIssueConditionByCode(
        issueAllocationRule.getCode(), issueAllocationRule.getIssueCondition());
  }
}
