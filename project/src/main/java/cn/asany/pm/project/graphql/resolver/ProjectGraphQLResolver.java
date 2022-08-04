package cn.asany.pm.project.graphql.resolver;

import cn.asany.pm.project.domain.Project;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

@Component
public class ProjectGraphQLResolver implements GraphQLResolver<Project> {

  //    @Autowired
  //    private IssueWorkflowSchemeService issueWorkflowSchemeService;
  //
  //    @Autowired
  //    private IssueProjectService issueProjectService;
  //
  //
  //    @Autowired
  //    private IssueResolutionService issueResolutionService;
  //
  //    @Autowired
  //    private IssueAllocationRuleService issueAllocationRuleService;
  //
  //    @Autowired
  //    private IssueAppraisalRuleService issueAppraisalRuleService;
  //
  //    @Autowired
  //    private IssueTypeCategoryService issueTypeCategoryService;

  //    public IssuePriority defaultPriority(IssueProject project) {
  //        return project.getPriorityScheme().getDefaultPriority();
  //    }

  //    public IssueType defaultIssueType(IssueProject project) {
  //        return project.getIssueTypeScheme().getDefaultType();
  //    }
  //
  //    public List<IssuePriority> priorities(IssueProject project) {
  //        return project.getPriorityScheme().getPriorities();
  //    }
  //
  //    public List<IssueTypeCategory> issueTypeCategories(IssueProject project) {
  //        return project.getIssueTypeScheme().getIssueTypeCategories();
  //    }
  //
  //
  //    public List<IssueType> issueTypes(IssueProject project) {
  //        return project.getIssueTypeScheme().getTypes();
  //    }
  //
  //    public List<IssueStatus> statuses(IssueProject project) {
  //        IssueProject issueProject = issueProjectService.get(project.getId()).orElse(null);
  //        return issueWorkflowSchemeService.findStatueAll(issueProject.getWorkflowScheme());
  //    }
  //
  //    public List<IssueResolution> resolutions(IssueProject project) {
  //        return issueResolutionService.findAll();
  //    }
  //
  //    /**
  //     * 自动派单规则
  //     */
  //    public List<IssueAllocationRule> issueAllocationRules(IssueProject project) {
  //        return issueAllocationRuleService.findAll();
  //    }
  //
  //    /**
  //     * 自动评价规则
  //     */
  //    public List<IssueAppraisalRule> issueAppraisalRules(IssueProject project) {
  //        return issueAppraisalRuleService.findAll();
  //    }
}
