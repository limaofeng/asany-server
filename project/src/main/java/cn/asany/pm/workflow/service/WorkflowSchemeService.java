package cn.asany.pm.workflow.service;

import cn.asany.pm.issue.attribute.domain.Status;
import cn.asany.pm.workflow.bean.Workflow;
import cn.asany.pm.workflow.bean.WorkflowAndIssueType;
import cn.asany.pm.workflow.bean.WorkflowScheme;
import cn.asany.pm.workflow.bean.WorkflowStep;
import cn.asany.pm.workflow.dao.IssueWorkflowSchemeDao;
import cn.asany.pm.workflow.dao.IssueWorkflowStepDao;
import cn.asany.pm.workflow.dao.WorkflowAndIssueTypeDao;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 工作流方案的service
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkflowSchemeService {

  @Autowired private IssueWorkflowSchemeDao issueWorkflowSchemeDao;

  @Autowired private WorkflowAndIssueTypeDao workflowAndIssueTypeDao;

  @Autowired private IssueWorkflowStepDao workflowStepDao;

  /**
   * 查询全部工作流方案
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public List<WorkflowScheme> issueWorkflowSchemes() {
    return issueWorkflowSchemeDao.findAll();
  }

  /**
   * 查询一个工作流方案
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public WorkflowScheme issueWorkflowScheme(Long id) {
    return issueWorkflowSchemeDao.findById(id).orElse(null);
  }

  /**
   * 创建工作流方案
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public WorkflowScheme createIssueWorkflowScheme(WorkflowScheme workflowScheme) {
    return issueWorkflowSchemeDao.save(workflowScheme);
  }

  /**
   * 编辑工作流方案
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public WorkflowScheme updateIssueWorkflowScheme(
      Long id, Boolean merge, WorkflowScheme workflowScheme) {
    workflowScheme.setId(id);
    return issueWorkflowSchemeDao.update(workflowScheme, merge);
  }

  /**
   * 删除工作流方案
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Boolean removeWorkflowScheme(Long id) {
    // 根据工作流方案id，删除工作流与任务类型的中间表
    List<WorkflowAndIssueType> all =
        workflowAndIssueTypeDao.findAll(
            Example.of(
                WorkflowAndIssueType.builder()
                    .workflowScheme(WorkflowScheme.builder().id(id).build())
                    .build()));
    if (all.size() > 0) {
      workflowAndIssueTypeDao.deleteAll(all);
    }
    // 删除工作流方案表中的数据
    issueWorkflowSchemeDao.deleteById(id);
    return true;
  }

  /**
   * 根据工作流方案id，查询该工作流方案中全部的状态
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public List<Status> findStatueAll(WorkflowScheme workflowScheme) {
    // 根据工作流方案，获取问题工作流
    List<WorkflowAndIssueType> workflows = workflowScheme.getWorkflows();
    if (workflows.size() > 0) {
      List<Status> list = new ArrayList<>();
      for (WorkflowAndIssueType type : workflows) {
        // 获取每条工作流
        Workflow workflow = type.getWorkflow();
        // 根据工作流查询全部工作流步骤
        List<WorkflowStep> allStep =
            workflowStepDao.findAll(Example.of(WorkflowStep.builder().workflow(workflow).build()));
        // 循环全部工作流步骤，获取状态
        for (WorkflowStep step : allStep) {
          Status state = step.getState();
          list.add(state);
        }
      }
      LinkedHashSet<Status> set = new LinkedHashSet<Status>(list.size());
      set.addAll(list);
      list.clear();
      list.addAll(set);
      return list;
    }
    return null;
  }
}
