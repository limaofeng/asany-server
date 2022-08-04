package cn.asany.pm.rule.service;

import cn.asany.pm.issue.core.domain.Issue;
import cn.asany.pm.issue.core.service.IssueService;
import cn.asany.pm.issue.type.service.IssueTypeService;
import cn.asany.pm.project.service.ProjectService;
import cn.asany.pm.rule.bean.IssueAllocationRule;
import cn.asany.pm.rule.bean.IssueAllocationRuleEnum;
import cn.asany.pm.rule.bean.IssueCondition;
import cn.asany.pm.rule.dao.IssueAllocationRuleDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Service
public class IssueAllocationRuleService {
  @Autowired public IssueAllocationRuleDao issueAllocationRuleDao;

  @Autowired private ProjectService projectService;

  @Autowired private IssueTypeService issueTypeService;

  //  @Autowired private EmployeeService employeeService;
  //  @Autowired private DepartmentService departmentService;
  //  @Autowired private SecurityService securityService;

  @Autowired private IssueService issueService;

  /** 更新规则 */
  @Transactional
  public Boolean updateIssueRule(Long id, Boolean merge, IssueAllocationRule issueRuleScheme) {
    issueRuleScheme.setId(id);
    issueAllocationRuleDao.update(issueRuleScheme, merge);
    return true;
  }

  /** 保存派单规则 */
  public IssueAllocationRule save(IssueAllocationRule issueAllocationRule) {
    issueAllocationRule.setPriority(issueAllocationRuleDao.count() + 1);
    return issueAllocationRuleDao.save(issueAllocationRule);
  }

  /** 派单排序 */
  @Transactional
  public Boolean sort(Long id, Long now) {
    IssueAllocationRule issueAllocationRule = issueAllocationRuleDao.getOne(id);
    if (issueAllocationRule.getPriority() > now) {
      issueAllocationRuleDao.rise(issueAllocationRule.getPriority(), now);
    } else {
      int a = issueAllocationRuleDao.decline(issueAllocationRule.getPriority(), now);
      System.out.println(a);
    }
    issueAllocationRule.setPriority(now);
    issueAllocationRuleDao.update(issueAllocationRule, true);
    return true;
  }

  /**
   * @param id
   * @return
   */
  @Transactional
  public Boolean delete(Long id) {
    issueAllocationRuleDao.resetSort(issueAllocationRuleDao.getOne(id).getPriority());
    issueAllocationRuleDao.deleteById(id);
    return true;
  }

  /** 查询所有 */
  public List<IssueAllocationRule> findAll() {
    return issueAllocationRuleDao.findAll(Sort.by("priority"));
  }

  /** 自动分配人 */
  public Long getIssueAssignee(Issue issue) {
    List<IssueAllocationRule> rules = issueAllocationRuleDao.findAll(Sort.by("priority"));
    for (IssueAllocationRule rule : rules) {
      Long assignee = getAllocationRule(rule, issue);
      if (assignee != null) {
        return assignee;
      }
    }
    return null;
  }

  /** 根据规则获取人 */
  public Long getAllocationRule(IssueAllocationRule issueAllocationRule, Issue issue) {
    Long id = null;
    switch (issueAllocationRule.getCode()) {
      case Type:
        id = issue.getType().getId();
        break;
      case Project:
        id = issue.getProject().getId();
        break;
      case Department:
        //        for (EmployeePosition employeePosition :
        //            employeeService.get(issue.getReporter().getId()).getEmployeePositions()) {
        //          if (employeePosition.getPrimary()) {
        //            id = employeePosition.getDepartment().getId();
        //          }
        //        }
        break;
    }
    //    if (issueAllocationRule.getIssueCondition().equals(id)) {
    //      List<Employee> employees =
    //          this.securityService.getSelection(issueAllocationRule.getSelectionScope());
    //      return issueService.assingeeMinIssue(employees);
    //    }
    return null;
  }

  /** 获取对应的规则 */
  public IssueCondition getIssueConditionByCode(IssueAllocationRuleEnum ruleEnum, Long id) {
    switch (ruleEnum) {
      case Type:
        return IssueCondition.builder()
            .name(issueTypeService.findById(id).getName())
            .id(id)
            .build();
      case Project:
        return IssueCondition.builder().name(projectService.findById(id).getName()).id(id).build();
        //      case Department:
        //        return
        // IssueCondition.builder().name(departmentService.get(id).getName()).id(id).build();
      default:
        return null;
    }
  }
}
