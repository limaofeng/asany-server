package cn.asany.pm.issue.core.graphql.resolver;

import cn.asany.pm.issue.core.domain.Issue;
import cn.asany.pm.issue.core.service.IssueService;
import cn.asany.pm.project.domain.ProjectMember;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 问题对象 Resolver
 *
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
@Component
public class IssueGraphQLResolver implements GraphQLResolver<Issue> {

  //  @Autowired private WorkflowService workflowService;

  @Autowired private IssueService issueService;

  //  public List<IssueOperation> operations(Issue issue, Long user) {
  //    List<WorkflowStepTransition> transitions =
  //        this.workflowService.getWorkflowStepTransitions(issue, user);
  //    List<IssueOperation> issueOperations =
  //        transitions.stream()
  //            .map(
  //                item ->
  //                    IssueOperation.builder()
  //                        .name(item.getName())
  //                        .issue(issue)
  //                        .user(user)
  //                        .id(item.getId())
  //                        .screen(item.getView())
  //                        .build())
  //            .collect(Collectors.toList());
  //    return issueOperations;
  //  }

  public ProjectMember assignee(Issue issue) {
    //        if (issue.getAssignee() != null) {
    //            return employeeService.get(issue.getAssignee());
    //        } else {
    //            return null;
    //        }
    return null;
  }

  public boolean startIssueFlag(Issue issue) {
    return issue.getTimeTrack().getTrackDate() == null;
  }

  public List<ProjectMember> assignableUsers(Issue issue) {
    // return issueService.assignableUsers(issue.getId());
    return null;
  }
}
