package cn.asany.pm.rule.graphql;

import cn.asany.pm.project.service.ProjectService;
import cn.asany.pm.rule.bean.*;
import cn.asany.pm.rule.service.IssueAppraisalRuleInfoService;
import cn.asany.pm.rule.service.IssueAppraisalRuleService;
import cn.asany.pm.rule.service.IssueMessageRuleService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Component
public class RuleGraphQLQueryResolver implements GraphQLQueryResolver {

  @Autowired private ProjectService projectService;

  //  @Autowired private DepartmentService departmentService;

  @Autowired private IssueMessageRuleService issueMessageRuleService;
  @Autowired private IssueAppraisalRuleService issueAppraisalRuleService;
  @Autowired private IssueAppraisalRuleInfoService issueAppraisalRuleInfoService;

  /** 查询所有自动派单规则 */
  public List<IssueCondition> issueAllocationRule(IssueAllocationRuleEnum code) {
    List<IssueCondition> list = new ArrayList<>();
    switch (code) {
      case Project:
        projectService
            .findAll()
            .forEach(
                value -> list.add(IssueCondition.builder().id(value.getId()).name("项目名称").build()));
        return list;
      case Type:
        //        projectService
        //            .get(1L)
        //            .orElse(null)
        //            .getIssueTypeScheme()
        //            .getTypes()
        //            .forEach(
        //                value ->
        //                    list.add(
        //
        // IssueCondition.builder().id(value.getId()).name(value.getName()).build()));
        return list;
        //      case Department:
        //        departmentService
        //            .findAllByOrg("1")
        //            .forEach(
        //                value ->
        //                    list.add(
        //
        // IssueCondition.builder().id(value.getId()).name(value.getName()).build()));
        //        return list;
      default:
        return null;
    }
  }

  /** */
  public List<IssueMessageRule> issueMessageRules() {
    return issueMessageRuleService.findAll();
  }

  /** 获取所有的自动评价规则 */
  public List<IssueAppraisalRule> issueAppraisalRules() {
    return issueAppraisalRuleService.findAll();
  }

  public List<IssueAppraisalRuleInfo> issueAppraisalRuleInfos() {
    return issueAppraisalRuleInfoService.findAll();
  }
}
