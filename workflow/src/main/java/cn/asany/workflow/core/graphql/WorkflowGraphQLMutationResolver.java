package cn.asany.workflow.core.graphql;

import graphql.kickstart.tools.GraphQLMutationResolver;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author penghanying @ClassName: WorkflowGraphQLMutationResolver @Description: (这里用一句话描述这个类的作用)
 * @date 2019/5/23
 */
@Component
public class WorkflowGraphQLMutationResolver implements GraphQLMutationResolver {
  // 状态的service
  @Autowired private IssueStatusService issueStateService;

  // 流程的service
  @Autowired private IssueWorkflowService issueWorkflowService;

  // 步骤的service
  @Autowired private IssueWorkflowStepService issueWorkflowStepService;

  // 跳转至下一步的操作
  @Autowired private IssueWorkflowStepTransitionService issueWorkflowStepTransitionService;

  // 流程方案的service
  @Autowired private IssueWorkflowSchemeService issueWorkflowSchemeService;

  // 工作流与任务类型表的service
  @Autowired private WorkflowAndIssueTypeService workflowAndIssueTypeService;

  @Autowired private WorkflowScheduleService workflowScheduleService;

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 添加状态
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public IssueStatus createIssueStatus(IssueStatus issueState) {
    return issueStateService.createIssueState(issueState);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 修改状态
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public IssueStatus updateIssueStatus(Long id, Boolean merge, IssueStatus issueState) {
    return issueStateService.updateIssueState(id, merge, issueState);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 删除状态
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public Boolean removeIssueStatus(Long id) {
    return issueStateService.removeIssueStatus(id);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 创建工作流
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public IssueWorkflow createIssueWorkflow(IssueWorkflow issueWorkflow) {
    return issueWorkflowService.createIssueWorkflow(issueWorkflow);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 修改工作流
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public IssueWorkflow updateIssueWorkflow(Long id, Boolean merge, IssueWorkflow issueWorkflow) {
    return issueWorkflowService.updateIssueWorkflow(id, merge, issueWorkflow);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 删除工作流
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public Boolean removeIssueWorkflow(Long id) {
    return issueWorkflowService.removeIssueWorkflow(id);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 添加步骤
   *
   * @param workflow 流程的id
   * @param name 步骤的名称
   * @param linkedStatus 状态的id
   * @author penghanying
   * @date 2019/5/23
   */
  public IssueWorkflowStep createIssueWorkflowStep(Long workflow, String name, Long linkedStatus) {
    return issueWorkflowStepService.createIssueWorkflowStep(workflow, name, linkedStatus);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 修改步骤
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public IssueWorkflowStep updateIssueWorkflowStep(Long id, Boolean merge, String name) {
    return issueWorkflowStepService.updateIssueWorkflowStep(id, merge, name);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 删除步骤
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public Boolean removeIssueWorkflowStep(Long id) {
    return issueWorkflowStepService.removeIssueWorkflowStep(id);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 添加步骤操作
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public IssueWorkflowStep createIssueWorkflowStepTransition(
      Long step, IssueWorkflowStepTransition issueWorkflowStepTransition) {
    return issueWorkflowStepTransitionService.createIssueWorkflowStepTransition(
        step, issueWorkflowStepTransition);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 修改步骤操作
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public IssueWorkflowStep updateIssueWorkflowStepTransition(
      Long id, Boolean merge, IssueWorkflowStepTransition issueWorkflowStepTransition) {
    return issueWorkflowStepTransitionService.updateIssueWorkflowStepTransition(
        id, merge, issueWorkflowStepTransition);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 删除步骤操作
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public Boolean removeIssueWorkflowStepTransition(Long id) {
    return issueWorkflowStepTransitionService.removeIssueWorkflowStepTransition(id);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 创建工作流方案
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public IssueWorkflowScheme createIssueWorkflowScheme(IssueWorkflowScheme issueWorkflowScheme) {
    return issueWorkflowSchemeService.createIssueWorkflowScheme(issueWorkflowScheme);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 编辑工作流方案
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public IssueWorkflowScheme updateIssueWorkflowScheme(
      Long id, Boolean merge, IssueWorkflowScheme issueWorkflowScheme) {
    return issueWorkflowSchemeService.updateIssueWorkflowScheme(id, merge, issueWorkflowScheme);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 编辑工作流方案时，选择流程，给工作流选择问题类型
   *
   * @param scheme 工作流方案的id
   * @param workflow 工作流的id
   * @param issueTypes 任务类型id
   * @author penghanying
   * @date 2019/5/23
   */
  public WorkflowAndIssueType createIssueWorkflowSchemeItem(
      Long scheme, Long workflow, List<Long> issueTypes) {
    return workflowAndIssueTypeService.createIssueWorkflowSchemeItem(scheme, workflow, issueTypes);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 修改工作流方案设置的流程选择的问题类型
   *
   * @param id 主键id
   * @param issueTypes 任务类型id
   * @author penghanying
   * @date 2019/5/23
   */
  public WorkflowAndIssueType updateIssueWorkflowSchemeItem(
      Long id, Boolean merge, List<Long> issueTypes) {
    return workflowAndIssueTypeService.updateIssueWorkflowSchemeItem(id, merge, issueTypes);
  }

  /**
   * @ClassName: WorkflowGraphQLMutationResolver @Description: 删除工作流方案设置的流程选择的问题类型
   *
   * @author penghanying
   * @date 2019/5/24
   */
  public Boolean removeIssueWorkflowSchemeItem(Long id) {
    return workflowAndIssueTypeService.removeIssueWorkflowSchemeItem(id);
  }

  /**
   * @ClassName: WorkflowGraphQLMutationResolver @Description: 删除工作流方案
   *
   * @author penghanying
   * @date 2019/5/24
   */
  public Boolean removeWorkflowScheme(Long id) {
    return issueWorkflowSchemeService.removeWorkflowScheme(id);
  }

  /**
   * @ClassName: WorkflowGraphQLMutationResolver @Description: 记录每一次操作的数据
   *
   * @author penghanying
   * @date 2019/5/26
   */
  public WorkflowSchedule saveWorkflowSchedule(WorkflowSchedule workflowSchedule) {
    return workflowScheduleService.saveWorkflowSchedule(workflowSchedule);
  }
}
