package cn.asany.workflow.core.graphql;

import com.coxautodev.graphql.tools.GraphQLResolver;
import cn.asany.pm.workflow.bean.WorkflowSchedule;
import net.whir.hos.organization.bean.Employee;
import net.whir.hos.organization.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng@msn.com @ClassName: WorkflowGraphQLResolver @Description: (这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
@Component
public class WorkflowGraphQLResolver implements GraphQLResolver<WorkflowSchedule> {

  @Autowired private EmployeeService employeeService;

  public Employee assignee(WorkflowSchedule schedule) {
    if (schedule.getAssignee() != null) {
      return employeeService.get(schedule.getAssignee());
    } else {
      return null;
    }
  }
}
