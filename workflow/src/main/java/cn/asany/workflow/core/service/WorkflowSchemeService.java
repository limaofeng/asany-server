package cn.asany.workflow.core.service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import cn.asany.workflow.core.dao.WorkflowStepDao;
import cn.asany.workflow.core.dao.WorkflowAndIssueTypeDao;
import cn.asany.workflow.core.domain.WorkflowScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author limaofeng@msn.com @ClassName: IssueWorkflowSchemeService @Description:
 *     工作流方案的service(这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkflowSchemeService {

  @Autowired private WorkflowSchemeDao issueWorkflowSchemeDao;

  @Autowired private WorkflowAndIssueTypeDao workflowAndIssueTypeDao;

  @Autowired private WorkflowStepDao workflowStepDao;

  /**
   * @ClassName: IssueWorkflowSchemeService @Description: 查询全部工作流方案
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public List<WorkflowScheme> issueWorkflowSchemes() {
    return issueWorkflowSchemeDao.findAll();
  }

  /**
   * @ClassName: IssueWorkflowSchemeService @Description: 查询一个工作流方案
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public IssueWorkflowScheme issueWorkflowScheme(Long id) {
    return issueWorkflowSchemeDao.findById(id).orElse(null);
  }

  /**
   * @ClassName: IssueWorkflowSchemeService @Description: 创建工作流方案
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public IssueWorkflowScheme createIssueWorkflowScheme(IssueWorkflowScheme issueWorkflowScheme) {
    return issueWorkflowSchemeDao.save(issueWorkflowScheme);
  }

  /**
   * @ClassName: IssueWorkflowSchemeService @Description: 编辑工作流方案
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public IssueWorkflowScheme updateIssueWorkflowScheme(
      Long id, Boolean merge, IssueWorkflowScheme issueWorkflowScheme) {
    issueWorkflowScheme.setId(id);
    return issueWorkflowSchemeDao.update(issueWorkflowScheme, merge);
  }

  /**
   * @ClassName: IssueWorkflowSchemeService @Description: 删除工作流方案
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
                    .workflowScheme(IssueWorkflowScheme.builder().id(id).build())
                    .build()));
    if (all.size() > 0) {
      workflowAndIssueTypeDao.deleteAll(all);
    }
    // 删除工作流方案表中的数据
    issueWorkflowSchemeDao.deleteById(id);
    return true;
  }

  /**
   * @ClassName: IssueWorkflowSchemeService @Description: 根据工作流方案id，查询该工作流方案中全部的状态
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public List<IssueStatus> findStatueAll(IssueWorkflowScheme workflowScheme) {
    // 根据工作流方案，获取问题工作流
    List<WorkflowAndIssueType> workflows = workflowScheme.getWorkflows();
    if (workflows.size() > 0) {
      List<IssueStatus> list = new ArrayList<>();
      for (WorkflowAndIssueType type : workflows) {
        // 获取每条工作流
        IssueWorkflow workflow = type.getWorkflow();
        // 根据工作流查询全部工作流步骤
        List<IssueWorkflowStep> allStep =
            workflowStepDao.findAll(
                Example.of(IssueWorkflowStep.builder().workflow(workflow).build()));
        // 循环全部工作流步骤，获取状态
        for (IssueWorkflowStep step : allStep) {
          IssueStatus state = step.getState();
          list.add(state);
        }
      }
      LinkedHashSet<IssueStatus> set = new LinkedHashSet<IssueStatus>(list.size());
      set.addAll(list);
      list.clear();
      list.addAll(set);
      return list;
    }
    return null;
  }
}
