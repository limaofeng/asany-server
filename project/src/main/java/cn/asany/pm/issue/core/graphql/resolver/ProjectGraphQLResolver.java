package cn.asany.pm.issue.core.graphql.resolver;

import cn.asany.pm.issue.core.domain.Issue;
import cn.asany.pm.issue.core.graphql.input.IssueFilter;
import cn.asany.pm.issue.core.service.IssueService;
import cn.asany.pm.project.domain.Project;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component("issue.ProjectGraphQLResolver")
public class ProjectGraphQLResolver implements GraphQLResolver<Project> {

  private final IssueService issueService;

  public ProjectGraphQLResolver(IssueService issueService) {
    this.issueService = issueService;
  }

  public List<Issue> issues(Project project, IssueFilter filter, int size, Sort orderBy) {
    filter.setProject(project.getId());
    return issueService.findAll(filter.build(), size, orderBy);
  }
}
