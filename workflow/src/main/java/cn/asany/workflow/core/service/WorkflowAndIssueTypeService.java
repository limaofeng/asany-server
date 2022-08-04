package cn.asany.workflow.core.service;

import java.util.ArrayList;
import java.util.List;
import cn.asany.pm.type.bean.IssueType;
import cn.asany.pm.type.dao.IssueTypeDao;
import cn.asany.pm.workflow.bean.IssueWorkflow;
import cn.asany.pm.workflow.bean.IssueWorkflowScheme;
import cn.asany.pm.workflow.bean.WorkflowAndIssueType;
import cn.asany.pm.workflow.dao.IssueWorkflowDao;
import cn.asany.pm.workflow.dao.IssueWorkflowSchemeDao;
import cn.asany.pm.workflow.dao.WorkflowAndIssueTypeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author limaofeng@msn.com @ClassName: WorkflowAndIssueTypeService @Description: (这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkflowAndIssueTypeService {

  @Autowired private WorkflowAndIssueTypeDao workflowAndIssueTypeDao;

  @Autowired private IssueWorkflowSchemeDao issueWorkflowSchemeDao;

  @Autowired private IssueWorkflowDao issueWorkflowDao;

  @Autowired private IssueTypeDao issueTypeDao;

  /**
   * @ClassName: WorkflowAndIssueTypeService @Description: 编辑工作流方案时，选择流程，给工作流选择问题类型
   *
   * @param scheme 工作流方案的id
   * @param workflow 工作流的id
   * @param issueTypes 任务类型id
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public WorkflowAndIssueType createIssueWorkflowSchemeItem(
      Long scheme, Long workflow, List<Long> issueTypes) {
    WorkflowAndIssueType workflowAndIssueType = new WorkflowAndIssueType();
    // 根据流程方案的id，查询流程方案是否存在
    IssueWorkflowScheme issueWorkflowScheme = issueWorkflowSchemeDao.findById(scheme).orElse(null);
    if (issueWorkflowScheme != null) {
      workflowAndIssueType.setWorkflowScheme(issueWorkflowScheme);
    }
    // 根据流程的id,查询流程是否存在
    IssueWorkflow issueWorkflow = issueWorkflowDao.findById(workflow).orElse(null);
    if (issueWorkflow != null) {
      workflowAndIssueType.setWorkflow(issueWorkflow);
    }
    List<IssueType> list = new ArrayList<>();
    if (issueTypes.size() > 0) {
      for (Long type : issueTypes) {
        // 根据任务类型id，查询任务类型是否存在
        IssueType issueType = issueTypeDao.findById(type).orElse(null);
        list.add(issueType);
      }
      workflowAndIssueType.setIssueTypes(list);
    }
    ;
    return workflowAndIssueTypeDao.save(workflowAndIssueType);
  }

  /**
   * @ClassName: WorkflowAndIssueTypeService @Description: 修改工作流方案设置的流程选择的问题类型
   *
   * @param id 主键id
   * @param issueTypes 任务类型id
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public WorkflowAndIssueType updateIssueWorkflowSchemeItem(
      Long id, Boolean merge, List<Long> issueTypes) {
    // 根据主键id，查询workflowAndIssueType
    WorkflowAndIssueType workflowAndIssueType = workflowAndIssueTypeDao.findById(id).orElse(null);

    List<IssueType> list = new ArrayList<>();
    if (issueTypes.size() > 0) {
      for (Long type : issueTypes) {
        // 根据任务类型id，查询任务类型是否存在
        IssueType issueType = issueTypeDao.findById(type).orElse(null);
        list.add(issueType);
      }
      workflowAndIssueType.setIssueTypes(list);
    }
    workflowAndIssueTypeDao.update(workflowAndIssueType, merge);
    return workflowAndIssueType;
  }

  /**
   * @ClassName: WorkflowAndIssueTypeService @Description: 删除工作流方案设置的流程选择的问题类型
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Boolean removeIssueWorkflowSchemeItem(Long id) {
    workflowAndIssueTypeDao.deleteById(id);
    return true;
  }
}
