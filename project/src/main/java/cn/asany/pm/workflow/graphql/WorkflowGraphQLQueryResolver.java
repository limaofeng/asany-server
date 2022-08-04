package cn.asany.pm.workflow.graphql;

import cn.asany.pm.issue.attribute.domain.Status;
import cn.asany.pm.workflow.bean.Workflow;
import cn.asany.pm.workflow.bean.WorkflowSchedule;
import cn.asany.pm.workflow.bean.WorkflowScheme;
import cn.asany.pm.workflow.service.WorkflowScheduleService;
import cn.asany.pm.workflow.service.WorkflowSchemeService;
import cn.asany.pm.workflow.service.WorkflowService;
import cn.asany.pm.workflow.service.WorkflowStepTransitionService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 工作流接口
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
@Component
public class WorkflowGraphQLQueryResolver implements GraphQLQueryResolver {

  // 工作流的service
  @Autowired private WorkflowService workflowService;

  // 工作流方案的service
  @Autowired private WorkflowSchemeService workflowSchemeService;

  @Autowired private WorkflowStepTransitionService workflowStepTransitionService;

  @Autowired private WorkflowScheduleService workflowScheduleService;
  /**
   * @ClassName: IssueTaskGraphQLQueryResolver @Description: 查询全部工作流
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public List<Workflow> issueWorkflows() {
    return workflowService.issueWorkflows();
  }

  /**
   * @ClassName: IssueTaskGraphQLQueryResolver @Description: 查询一个工作流
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Workflow issueWorkflow(Long id) {
    return workflowService.issueWorkflow(id);
  }

  /**
   * @ClassName: IssueTaskGraphQLQueryResolver @Description: 查询全部工作流方案
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public List<WorkflowScheme> issueWorkflowSchemes() {
    return workflowSchemeService.issueWorkflowSchemes();
  }

  /**
   * @ClassName: IssueTaskGraphQLQueryResolver @Description: 查询一个工作流方案
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public WorkflowScheme issueWorkflowScheme(Long id) {
    return workflowSchemeService.issueWorkflowScheme(id);
  }

  /**
   * 根据问题id,操作id,查询状态
   *
   * @param type 问题id
   * @param tran 操作的id
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Status issueWorkflowStatus(Long workflow, Long type, Long tran) {
    return workflowStepTransitionService.issueWorkflowStatus(workflow, type, tran);
  }

  /**
   * 查询初始化状态 方案的id，问题类型的id
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Status issueInitializateStatus(Long scheme, Long type) {
    return workflowStepTransitionService.issueInitializateStatus(scheme, type);
  }

  /**
   * 根据问题id,项目id，查询所有操作记录
   *
   * @param issue 问题
   * @param project 项目的id
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public List<WorkflowSchedule> issueOperateLog(Long issue, Long project) {
    return workflowScheduleService.issueOperateLog(issue, project);
  }
}
