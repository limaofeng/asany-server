package cn.asany.pm.workflow.service;

import cn.asany.pm.issue.attribute.dao.StatusDao;
import cn.asany.pm.issue.attribute.domain.Status;
import cn.asany.pm.workflow.bean.Workflow;
import cn.asany.pm.workflow.bean.WorkflowStep;
import cn.asany.pm.workflow.dao.IssueWorkflowDao;
import cn.asany.pm.workflow.dao.IssueWorkflowStepDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 流程的步骤
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkflowStepService {

  @Autowired private IssueWorkflowStepDao workflowStepDao;

  @Autowired private IssueWorkflowDao issueWorkflowDao;

  @Autowired private StatusDao issueStateDao;

  /**
   * @ClassName: IssueWorkflowStepService @Description: 添加流程步骤
   *
   * @param workflow 流程的id
   * @param name 步骤的名称
   * @param linkedStatus 状态的id
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public WorkflowStep createIssueWorkflowStep(Long workflow, String name, Long linkedStatus) {
    WorkflowStep workflowStep = new WorkflowStep();
    // 根据流程id，查询流程是否存在
    Workflow issueWorkflow = issueWorkflowDao.findById(workflow).orElse(null);
    if (issueWorkflow != null) {
      workflowStep.setWorkflow(issueWorkflow);
    }
    // 根据状态id，查询状态是否存在
    Status issueState = issueStateDao.findById(linkedStatus).orElse(null);
    if (issueState != null) {
      workflowStep.setState(issueState);
    }
    workflowStep.setName(name);
    return workflowStepDao.save(workflowStep);
  }

  /**
   * 修改步骤
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public WorkflowStep updateIssueWorkflowStep(Long id, Boolean merge, String name) {
    // 根据步骤id，查询步骤
    WorkflowStep workflowStep = workflowStepDao.findById(id).orElse(null);
    if (workflowStep != null) {
      workflowStep.setId(id);
      workflowStep.setName(name);
    }
    return workflowStepDao.update(workflowStep, merge);
  }

  /**
   * 删除步骤
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Boolean removeIssueWorkflowStep(Long id) {
    workflowStepDao.deleteById(id);
    return true;
  }
}
