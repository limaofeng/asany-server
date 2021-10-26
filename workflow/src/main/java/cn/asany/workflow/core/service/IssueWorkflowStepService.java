package cn.asany.workflow.core.service;

import net.whir.hos.issue.attribute.bean.IssueStatus;
import net.whir.hos.issue.attribute.dao.IssueStatusDao;
import net.whir.hos.issue.workflow.bean.IssueWorkflow;
import net.whir.hos.issue.workflow.bean.IssueWorkflowStep;
import net.whir.hos.issue.workflow.dao.IssueWorkflowDao;
import net.whir.hos.issue.workflow.dao.IssueWorkflowStepDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author penghanying @ClassName: IssueWorkflowStepService @Description: 流程的步骤(这里用一句话描述这个类的作用)
 * @date 2019/5/23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IssueWorkflowStepService {

  @Autowired private IssueWorkflowStepDao workflowStepDao;

  @Autowired private IssueWorkflowDao issueWorkflowDao;

  @Autowired private IssueStatusDao issueStateDao;

  /**
   * @ClassName: IssueWorkflowStepService @Description: 添加流程步骤
   *
   * @param workflow 流程的id
   * @param name 步骤的名称
   * @param linkedStatus 状态的id
   * @author penghanying
   * @date 2019/5/23
   */
  public IssueWorkflowStep createIssueWorkflowStep(Long workflow, String name, Long linkedStatus) {
    IssueWorkflowStep issueWorkflowStep = new IssueWorkflowStep();
    // 根据流程id，查询流程是否存在
    IssueWorkflow issueWorkflow = issueWorkflowDao.findById(workflow).orElse(null);
    if (issueWorkflow != null) {
      issueWorkflowStep.setWorkflow(issueWorkflow);
    }
    // 根据状态id，查询状态是否存在
    IssueStatus issueState = issueStateDao.findById(linkedStatus).orElse(null);
    if (issueState != null) {
      issueWorkflowStep.setState(issueState);
    }
    issueWorkflowStep.setName(name);
    return workflowStepDao.save(issueWorkflowStep);
  }

  /**
   * @ClassName: IssueWorkflowStepService @Description: 修改步骤
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public IssueWorkflowStep updateIssueWorkflowStep(Long id, Boolean merge, String name) {
    // 根据步骤id，查询步骤
    IssueWorkflowStep issueWorkflowStep = workflowStepDao.findById(id).orElse(null);
    if (issueWorkflowStep != null) {
      issueWorkflowStep.setId(id);
      issueWorkflowStep.setName(name);
    }
    return workflowStepDao.update(issueWorkflowStep, merge);
  }

  /**
   * @ClassName: IssueWorkflowStepService @Description: 删除步骤
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public Boolean removeIssueWorkflowStep(Long id) {
    workflowStepDao.deleteById(id);
    return true;
  }
}
