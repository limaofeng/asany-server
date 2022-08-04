package cn.asany.workflow.core.service;

import java.util.List;
import java.util.Optional;
import cn.asany.pm.attribute.bean.IssueStatus;
import cn.asany.pm.workflow.bean.*;
import cn.asany.pm.workflow.dao.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author limaofeng@msn.com @ClassName: IssueWorkflowStepTransitionService @Description: (这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IssueWorkflowStepTransitionService {

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
  public IssueWorkflowStep createIssueWorkflowStepTransition(
      Long step, IssueWorkflowStepTransition issueWorkflowStepTransition) {
    issueWorkflowStepTransition.setStep(issueWorkflowStepDao.findById(step).orElse(null));
    IssueWorkflowStepTransition save =
        issueWorkflowStepTransitionDao.save(issueWorkflowStepTransition);
    return save.getStep();
  }

  /**
   * @ClassName: IssueWorkflowStepTransitionService @Description: 修改步骤操作
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public IssueWorkflowStep updateIssueWorkflowStepTransition(
      Long id, Boolean merge, IssueWorkflowStepTransition issueWorkflowStepTransition) {
    issueWorkflowStepTransition.setId(id);
    IssueWorkflowStepTransition update =
        issueWorkflowStepTransitionDao.update(issueWorkflowStepTransition, merge);
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

  /**
   * @ClassName: IssueWorkflowStepTransitionService @Description: 根据工作流方案id, 问题id, 操作id, 查询状态
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public IssueStatus issueWorkflowStatus(Long scheme, Long issueType, Long tran) {
    // 根据问题类型的id,查询流程id
    IssueWorkflowScheme issueWorkflowScheme = issueWorkflowSchemeDao.findById(scheme).orElse(null);
    // 获取问题工作流
    Optional<WorkflowAndIssueType> optionalWorkflowAndIssueType =
        issueWorkflowScheme.getWorkflows().stream()
            .filter(
                item ->
                    item.getIssueTypes().stream().anyMatch(type -> type.getId().equals(issueType)))
            .findAny();
    IssueWorkflow workflow;
    if (optionalWorkflowAndIssueType.isPresent()) {
      workflow = optionalWorkflowAndIssueType.get().getWorkflow();
    } else {
      // 没有给个默认的方案
      workflow = workflowAndIssueTypeDao.getOne(1L).getWorkflow();
      //    throw new RuntimeException("需要匹配默认的问题方案");
    }

    // 获取流程中包含的步骤
    List<IssueWorkflowStep> steps = workflow.getSteps();
    for (IssueWorkflowStep step : steps) {
      // 根据步骤id，查询步骤拥有的操作
      IssueWorkflowStep issueWorkflowStep =
          issueWorkflowStepDao.findById(step.getId()).orElse(null);
      List<IssueWorkflowStepTransition> transitions = issueWorkflowStep.getTransitions();
      for (IssueWorkflowStepTransition transition : transitions) {
        if (transition.getId().equals(tran)) {
          IssueWorkflowStepTransition byId =
              issueWorkflowStepTransitionDao.findById(tran).orElse(null);
          return byId.getDestination().getState();
        }
      }
    }
    return null;
  }

  /**
   * @ClassName: IssueWorkflowStepTransitionService @Description: 查询初始化状态 方案的id，问题类型的id
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public IssueStatus issueInitializateStatus(Long scheme, Long issueType) {
    // 根据方案的id,查询流程id
    IssueWorkflowScheme issueWorkflowScheme = issueWorkflowSchemeDao.findById(scheme).orElse(null);
    // 获取问题工作流
    Optional<WorkflowAndIssueType> optionalWorkflowAndIssueType =
        issueWorkflowScheme.getWorkflows().stream()
            .filter(
                item ->
                    item.getIssueTypes().stream().anyMatch(type -> type.getId().equals(issueType)))
            .findAny();
    IssueWorkflow workflow;
    if (optionalWorkflowAndIssueType.isPresent()) {
      workflow = optionalWorkflowAndIssueType.get().getWorkflow();
    } else {
      workflow = workflowAndIssueTypeDao.getOne(1L).getWorkflow();
      //            throw new RuntimeException("需要匹配默认的问题方案");
    }

    // 根据工作流，查询工作流的步骤
    List<IssueWorkflowStep> steps = workflow.getSteps();
    if (steps.size() > 0) {
      return steps.get(0).getState();
    }
    return null;
  }
}
