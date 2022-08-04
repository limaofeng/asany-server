package cn.asany.pm.workflow.graphql;

import cn.asany.pm.issue.attribute.domain.Status;
import cn.asany.pm.issue.attribute.service.StatusService;
import cn.asany.pm.workflow.bean.*;
import cn.asany.pm.workflow.service.*;
import graphql.kickstart.tools.GraphQLMutationResolver;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 工作流 API 接口
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
@Component
public class WorkflowGraphQLMutationResolver implements GraphQLMutationResolver {
  // 状态的service
  @Autowired private StatusService statusService;

  // 流程的service
  @Autowired private WorkflowService workflowService;

  // 步骤的service
  @Autowired private WorkflowStepService workflowStepService;

  // 跳转至下一步的操作
  @Autowired private WorkflowStepTransitionService workflowStepTransitionService;

  // 流程方案的service
  @Autowired private WorkflowSchemeService workflowSchemeService;

  // 工作流与任务类型表的service
  @Autowired private WorkflowAndIssueTypeService workflowAndIssueTypeService;

  @Autowired private WorkflowScheduleService workflowScheduleService;

  /** 添加状态 */
  public Status createIssueStatus(Status issueState) {
    return statusService.createIssueState(issueState);
  }

  /** 修改状态 */
  public Status updateIssueStatus(Long id, Boolean merge, Status issueState) {
    return statusService.updateIssueState(id, merge, issueState);
  }

  /** 删除状态 */
  public Boolean removeIssueStatus(Long id) {
    return statusService.removeStatus(id);
  }

  /** 创建工作流 */
  public Workflow createIssueWorkflow(Workflow workflow) {
    return workflowService.createIssueWorkflow(workflow);
  }

  /** 修改工作流 */
  public Workflow updateIssueWorkflow(Long id, Boolean merge, Workflow workflow) {
    return workflowService.updateIssueWorkflow(id, merge, workflow);
  }

  /** 删除工作流 */
  public Boolean removeIssueWorkflow(Long id) {
    return workflowService.removeIssueWorkflow(id);
  }

  /**
   * 添加步骤
   *
   * @param workflow 流程的id
   * @param name 步骤的名称
   * @param linkedStatus 状态的id
   */
  public WorkflowStep createIssueWorkflowStep(Long workflow, String name, Long linkedStatus) {
    return workflowStepService.createIssueWorkflowStep(workflow, name, linkedStatus);
  }

  /** 修改步骤 */
  public WorkflowStep updateIssueWorkflowStep(Long id, Boolean merge, String name) {
    return workflowStepService.updateIssueWorkflowStep(id, merge, name);
  }

  /** 删除步骤 */
  public Boolean removeIssueWorkflowStep(Long id) {
    return workflowStepService.removeIssueWorkflowStep(id);
  }

  /** 添加步骤操作 */
  public WorkflowStep createIssueWorkflowStepTransition(
      Long step, WorkflowStepTransition workflowStepTransition) {
    return workflowStepTransitionService.createIssueWorkflowStepTransition(
        step, workflowStepTransition);
  }

  /** 修改步骤操作 */
  public WorkflowStep updateIssueWorkflowStepTransition(
      Long id, Boolean merge, WorkflowStepTransition workflowStepTransition) {
    return workflowStepTransitionService.updateIssueWorkflowStepTransition(
        id, merge, workflowStepTransition);
  }

  /** 删除步骤操作 */
  public Boolean removeIssueWorkflowStepTransition(Long id) {
    return workflowStepTransitionService.removeIssueWorkflowStepTransition(id);
  }

  /** 创建工作流方案 */
  public WorkflowScheme createIssueWorkflowScheme(WorkflowScheme workflowScheme) {
    return workflowSchemeService.createIssueWorkflowScheme(workflowScheme);
  }

  /** 编辑工作流方案 */
  public WorkflowScheme updateIssueWorkflowScheme(
      Long id, Boolean merge, WorkflowScheme workflowScheme) {
    return workflowSchemeService.updateIssueWorkflowScheme(id, merge, workflowScheme);
  }

  /**
   * 编辑工作流方案时，选择流程，给工作流选择问题类型
   *
   * @param scheme 工作流方案的id
   * @param workflow 工作流的id
   * @param issueTypes 任务类型id
   */
  public WorkflowAndIssueType createIssueWorkflowSchemeItem(
      Long scheme, Long workflow, List<Long> issueTypes) {
    return workflowAndIssueTypeService.createIssueWorkflowSchemeItem(scheme, workflow, issueTypes);
  }

  /**
   * 修改工作流方案设置的流程选择的问题类型
   *
   * @param id 主键id
   * @param issueTypes 任务类型id
   */
  public WorkflowAndIssueType updateIssueWorkflowSchemeItem(
      Long id, Boolean merge, List<Long> issueTypes) {
    return workflowAndIssueTypeService.updateIssueWorkflowSchemeItem(id, merge, issueTypes);
  }

  /** 删除工作流方案设置的流程选择的问题类型 */
  public Boolean removeIssueWorkflowSchemeItem(Long id) {
    return workflowAndIssueTypeService.removeIssueWorkflowSchemeItem(id);
  }

  /** 删除工作流方案 */
  public Boolean removeWorkflowScheme(Long id) {
    return workflowSchemeService.removeWorkflowScheme(id);
  }

  /** 记录每一次操作的数据 */
  public WorkflowSchedule saveWorkflowSchedule(WorkflowSchedule workflowSchedule) {
    return workflowScheduleService.saveWorkflowSchedule(workflowSchedule);
  }
}
