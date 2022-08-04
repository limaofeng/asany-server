package cn.asany.workflow.core.graphql;

import cn.asany.workflow.core.domain.Workflow;
import cn.asany.workflow.core.domain.WorkflowScheme;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import java.util.List;
import cn.asany.pm.attribute.bean.IssueStatus;
import cn.asany.pm.workflow.bean.IssueWorkflow;
import cn.asany.pm.workflow.bean.IssueWorkflowScheme;
import cn.asany.pm.workflow.bean.WorkflowSchedule;
import cn.asany.pm.workflow.service.IssueWorkflowSchemeService;
import cn.asany.pm.workflow.service.IssueWorkflowService;
import cn.asany.pm.workflow.service.IssueWorkflowStepTransitionService;
import cn.asany.pm.workflow.service.WorkflowScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng@msn.com @ClassName: WorkflowGraphQLQueryResolver @Description: (这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
@Component
public class WorkflowGraphQLQueryResolver implements GraphQLQueryResolver {

  // 工作流的service
  @Autowired private IssueWorkflowService issueWorkflowService;

  // 工作流方案的service
  @Autowired private IssueWorkflowSchemeService issueWorkflowSchemeService;

  @Autowired private IssueWorkflowStepTransitionService issueWorkflowStepTransitionService;

  @Autowired private WorkflowScheduleService workflowScheduleService;
  /**
   * @ClassName: IssueTaskGraphQLQueryResolver @Description: 查询全部工作流
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public List<IssueWorkflow> issueWorkflows() {
    return issueWorkflowService.issueWorkflows();
  }

  /**
   * @ClassName: IssueTaskGraphQLQueryResolver @Description: 查询一个工作流
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Workflow issueWorkflow(Long id) {
    return issueWorkflowService.issueWorkflow(id);
  }

  /**
   * @ClassName: IssueTaskGraphQLQueryResolver @Description: 查询全部工作流方案
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public List<WorkflowScheme> issueWorkflowSchemes() {
    return issueWorkflowSchemeService.issueWorkflowSchemes();
  }

  /**
   * @ClassName: IssueTaskGraphQLQueryResolver @Description: 查询一个工作流方案
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public IssueWorkflowScheme issueWorkflowScheme(Long id) {
    return issueWorkflowSchemeService.issueWorkflowScheme(id);
  }

  /**
   * @ClassName: WorkflowGraphQLQueryResolver @Description: 根据问题id,操作id,查询状态
   *
   * @param type 问题id
   * @param tran 操作的id
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public IssueStatus issueWorkflowStatus(Long workflow, Long type, Long tran) {
    return issueWorkflowStepTransitionService.issueWorkflowStatus(workflow, type, tran);
  }

  /**
   * @ClassName: WorkflowGraphQLQueryResolver @Description: 查询初始化状态 方案的id，问题类型的id
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public IssueStatus issueInitializateStatus(Long scheme, Long type) {
    return issueWorkflowStepTransitionService.issueInitializateStatus(scheme, type);
  }

  /**
   * @ClassName: WorkflowGraphQLMutationResolver @Description: 根据问题id,项目id，查询所有操作记录
   *
   * @param
   * @param project 项目的id
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public List<WorkflowSchedule> issueOperateLog(Long issue, Long project) {
    return workflowScheduleService.issueOperateLog(issue, project);
  }
}
