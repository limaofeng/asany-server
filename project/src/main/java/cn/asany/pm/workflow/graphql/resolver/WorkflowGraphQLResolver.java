package cn.asany.pm.workflow.graphql.resolver;

import cn.asany.pm.project.domain.ProjectMember;
import cn.asany.pm.workflow.bean.WorkflowSchedule;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng@msn.com @ClassName: WorkflowGraphQLResolver @Description: (这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
@Component
public class WorkflowGraphQLResolver implements GraphQLResolver<WorkflowSchedule> {

  public ProjectMember assignee(WorkflowSchedule schedule) {
    //    if (schedule.getAssignee() != null) {
    //      return employeeService.get(schedule.getAssignee());
    //    } else {
    //      return null;
    //    }
    return null;
  }
}
