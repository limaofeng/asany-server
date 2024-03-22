package cn.asany.pm.workflow.service;

import cn.asany.pm.issue.core.domain.Issue;
import cn.asany.pm.security.bean.Permission;
import cn.asany.pm.security.dao.PermissionDao;
import cn.asany.pm.workflow.bean.*;
import cn.asany.pm.workflow.dao.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author limaofeng@msn.com @ClassName: IssueWorkflowService @Description:
 *     工作流的service(这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class WorkflowService {

  @Autowired private IssueWorkflowDao issueWorkflowDao;

  @Autowired private WorkflowAndIssueTypeDao workflowAndIssueTypeDao;

  @Autowired private IssueWorkflowSchemeDao issueWorkflowSchemeDao;

  @Autowired private IssueWorkflowStepDao issueWorkflowStepDao;

  @Autowired private IssueWorkflowStepTransitionDao transitionDao;

  @Autowired private IssueWorkflowStepTransitionConditionDao workflowStepTransitionConditionDao;

  @Autowired private PermissionDao permissionDao;

  /**
   * @ClassName: IssueWorkflowService @Description: 查询全部工作流
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public List<Workflow> issueWorkflows() {
    return issueWorkflowDao.findAll();
  }

  /**
   * @ClassName: IssueWorkflowService @Description: 查询一个工作流
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Workflow issueWorkflow(Long id) {
    return issueWorkflowDao.findById(id).orElse(null);
  }

  /**
   * @ClassName: IssueWorkflowService @Description: 创建工作流
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Workflow createIssueWorkflow(Workflow workflow) {
    return issueWorkflowDao.save(workflow);
  }

  /**
   * @ClassName: IssueWorkflowService @Description: 修改工作流
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Workflow updateIssueWorkflow(Long id, Boolean merge, Workflow workflow) {
    workflow.setId(id);
    return issueWorkflowDao.update(workflow, merge);
  }

  /**
   * @ClassName: IssueWorkflowService @Description: 删除工作流
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Boolean removeIssueWorkflow(Long id) {
    // 根据工作流id，删除工作流与任务类型的中间表
    List<WorkflowAndIssueType> all =
        workflowAndIssueTypeDao.findAll(
            Example.of(
                WorkflowAndIssueType.builder()
                    .workflow(Workflow.builder().id(id).build())
                    .build()));
    if (all.size() > 0) {
      workflowAndIssueTypeDao.deleteAll(all);
    }
    // 删除工作流中的数据
    issueWorkflowDao.deleteById(id);
    return true;
  }

  /**
   * 获取问题当前对应的操作
   *
   * @param issue
   * @return
   */
  public List<WorkflowStepTransition> getWorkflowStepTransitions(Issue issue, Long user) {
    WorkflowScheme workflowScheme = null; // issue.getProject().getWorkflowScheme();

    if (workflowScheme == null) {
      workflowScheme = issueWorkflowSchemeDao.findById(1L).orElse(null);
    }

    // 获取问题工作流
    Optional<WorkflowAndIssueType> optionalWorkflowAndIssueType =
        workflowScheme.getWorkflows().stream()
            .filter(
                item ->
                    item.getIssueTypes().stream()
                        .anyMatch(type -> type.getId().equals(issue.getType().getId())))
            .findAny();
    Workflow workflow;
    if (optionalWorkflowAndIssueType.isPresent()) {
      workflow = optionalWorkflowAndIssueType.get().getWorkflow();
    } else {
      workflow = workflowScheme.getDefaultWorkflow();
    }

    // 获取对应的步骤
    Optional<WorkflowStep> issueWorkflowStep =
        workflow.getSteps().stream()
            .filter(item -> item.getState().getId().equals(issue.getStatus().getId()))
            .findAny();

    if (issueWorkflowStep.isPresent()) {
      // 查询该步骤应该有的全部操作
      List<WorkflowStepTransition> all =
          transitionDao.findAll(
              Example.of(WorkflowStepTransition.builder().step(issueWorkflowStep.get()).build()));
      return all;
    } else {
      log.error("需要匹配默认的问题方案");
      return new ArrayList<>();
    }
  }

  /**
   * @ClassName: IssueWorkflowService @Description: 通过操作的id，查询权限
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public List<Permission> getGrants(Long id) {
    List<WorkflowStepTransitionCondition> conditionDaoAll =
        workflowStepTransitionConditionDao.findAll(
            Example.of(WorkflowStepTransitionCondition.builder().id(id).build()));
    List<Permission> permissions = new ArrayList<>();
    if (conditionDaoAll.size() > 0) {
      for (WorkflowStepTransitionCondition condition : conditionDaoAll) {
        String value = condition.getValue();
        List<Permission> all =
            permissionDao.findAll(Example.of(Permission.builder().code(value).build()));
        for (Permission permission : all) {
          permissions.add(permission);
        }
      }
      return permissions;
    }
    return null;
  }
}
