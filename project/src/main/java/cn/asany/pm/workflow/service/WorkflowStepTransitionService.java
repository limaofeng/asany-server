package cn.asany.pm.workflow.service;

import cn.asany.pm.issue.attribute.domain.Status;
import cn.asany.pm.workflow.bean.*;
import cn.asany.pm.workflow.dao.*;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 工作流步骤
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkflowStepTransitionService {

  @Autowired private IssueWorkflowStepTransitionDao issueWorkflowStepTransitionDao;

  @Autowired private IssueWorkflowStepDao issueWorkflowStepDao;

  @Autowired private IssueWorkflowDao issueWorkflowDao;

  @Autowired private WorkflowAndIssueTypeDao workflowAndIssueTypeDao;

  @Autowired private IssueWorkflowSchemeDao issueWorkflowSchemeDao;

  /**
   * @ClassName: IssueWorkflowStepTransitionService @Description: 添加步骤操作
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public WorkflowStep createIssueWorkflowStepTransition(
      Long step, WorkflowStepTransition workflowStepTransition) {
    workflowStepTransition.setStep(issueWorkflowStepDao.findById(step).orElse(null));
    WorkflowStepTransition save = issueWorkflowStepTransitionDao.save(workflowStepTransition);
    return save.getStep();
  }

  /**
   * @ClassName: IssueWorkflowStepTransitionService @Description: 修改步骤操作
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public WorkflowStep updateIssueWorkflowStepTransition(
      Long id, Boolean merge, WorkflowStepTransition workflowStepTransition) {
    workflowStepTransition.setId(id);
    WorkflowStepTransition update =
        issueWorkflowStepTransitionDao.update(workflowStepTransition, merge);
    return update.getStep();
  }

  /**
   * @ClassName: IssueWorkflowStepTransitionService @Description: 删除步骤操作
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Boolean removeIssueWorkflowStepTransition(Long id) {
    issueWorkflowStepTransitionDao.deleteById(id);
    return true;
  }

  /** 根据工作流方案id, 问题id, 操作id, 查询状态 */
  public Status issueWorkflowStatus(Long scheme, Long issueType, Long tran) {
    // 根据问题类型的id,查询流程id
    WorkflowScheme workflowScheme = issueWorkflowSchemeDao.findById(scheme).orElse(null);
    // 获取问题工作流
    Optional<WorkflowAndIssueType> optionalWorkflowAndIssueType =
        workflowScheme.getWorkflows().stream()
            .filter(
                item ->
                    item.getIssueTypes().stream().anyMatch(type -> type.getId().equals(issueType)))
            .findAny();
    Workflow workflow;
    if (optionalWorkflowAndIssueType.isPresent()) {
      workflow = optionalWorkflowAndIssueType.get().getWorkflow();
    } else {
      // 没有给个默认的方案
      workflow = workflowAndIssueTypeDao.getOne(1L).getWorkflow();
      //    throw new RuntimeException("需要匹配默认的问题方案");
    }

    // 获取流程中包含的步骤
    List<WorkflowStep> steps = workflow.getSteps();
    for (WorkflowStep step : steps) {
      // 根据步骤id，查询步骤拥有的操作
      WorkflowStep workflowStep = issueWorkflowStepDao.findById(step.getId()).orElse(null);
      List<WorkflowStepTransition> transitions = workflowStep.getTransitions();
      for (WorkflowStepTransition transition : transitions) {
        if (transition.getId().equals(tran)) {
          WorkflowStepTransition byId = issueWorkflowStepTransitionDao.findById(tran).orElse(null);
          return byId.getDestination().getState();
        }
      }
    }
    return null;
  }

  /** 查询初始化状态 方案的id，问题类型的id */
  public Status issueInitializateStatus(Long scheme, Long issueType) {
    // 根据方案的id,查询流程id
    WorkflowScheme workflowScheme = issueWorkflowSchemeDao.findById(scheme).orElse(null);
    // 获取问题工作流
    Optional<WorkflowAndIssueType> optionalWorkflowAndIssueType =
        workflowScheme.getWorkflows().stream()
            .filter(
                item ->
                    item.getIssueTypes().stream().anyMatch(type -> type.getId().equals(issueType)))
            .findAny();
    Workflow workflow;
    if (optionalWorkflowAndIssueType.isPresent()) {
      workflow = optionalWorkflowAndIssueType.get().getWorkflow();
    } else {
      workflow = workflowAndIssueTypeDao.getOne(1L).getWorkflow();
      //            throw new RuntimeException("需要匹配默认的问题方案");
    }

    // 根据工作流，查询工作流的步骤
    List<WorkflowStep> steps = workflow.getSteps();
    if (steps.size() > 0) {
      return steps.get(0).getState();
    }
    return null;
  }
}
