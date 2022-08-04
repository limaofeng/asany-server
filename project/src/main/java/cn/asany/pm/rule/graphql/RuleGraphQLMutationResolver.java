package cn.asany.pm.rule.graphql;

import cn.asany.pm.rule.bean.*;
import cn.asany.pm.rule.service.IssueAllocationRuleService;
import cn.asany.pm.rule.service.IssueAppraisalRuleService;
import cn.asany.pm.rule.service.IssueMessageRuleService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Component
public class RuleGraphQLMutationResolver implements GraphQLMutationResolver {

  @Autowired private IssueAllocationRuleService issueAllocationRuleDaoService;

  @Autowired private IssueMessageRuleService issueMessageRuleService;
  @Autowired private IssueAppraisalRuleService issueAppraisalRuleService;

  /** 新增派单规则 */
  public IssueAllocationRule careateIssueAllocationRule(IssueAllocationRule issueAllocationRule) {
    return issueAllocationRuleDaoService.save(issueAllocationRule);
  }

  /**
   * @param ruleId
   * @param issueAllocationRule
   * @return
   */
  public Boolean updateIssueAllocationRule(Long ruleId, IssueAllocationRule issueAllocationRule) {
    return issueAllocationRuleDaoService.updateIssueRule(ruleId, true, issueAllocationRule);
  }

  public Boolean removeIssueAllocationRule(Long ruleId) {
    return issueAllocationRuleDaoService.delete(ruleId);
  }

  public Boolean issueAllocationRuleSort(Long id, Long now) {
    return issueAllocationRuleDaoService.sort(id, now);
  }
  /** 修改消息提醒 */
  public Boolean updateIssueMessageRule(
      Long ruleId, Boolean merge, IssueMessageRule issueMessageRule) {
    return issueMessageRuleService.updateIssueMessageRule(ruleId, merge, issueMessageRule);
  }
  /** 修改自动评价规则 */
  public Boolean updateIssueAppraisalRule(
      Long ruleId, Boolean merge, IssueAppraisalRule issueAppraisalRule) {
    return issueAppraisalRuleService.update(ruleId, merge, issueAppraisalRule);
  }
}
