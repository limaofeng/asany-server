package cn.asany.pm.issue.type.graphql;

import cn.asany.pm.issue.type.domain.IssueType;
import cn.asany.pm.issue.type.domain.IssueTypeScheme;
import cn.asany.pm.issue.type.service.IssueTypeSchemeService;
import cn.asany.pm.issue.type.service.IssueTypeService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 问题类型
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
@Component
public class TypeGraphQLMutationResolver implements GraphQLMutationResolver {
  /** 任务类型的Service */
  @Autowired private IssueTypeService taskTypeService;

  /** 任务类型方案的service */
  @Autowired private IssueTypeSchemeService issueTypeSchemeService;

  /** 新增任务类型 */
  public IssueType createIssueType(Long schemId, Long categoryId, IssueType issueType) {
    return taskTypeService.createTaskType(schemId, categoryId, issueType);
  }

  /** 修改任务类型 */
  public IssueType updateIssueType(Long id, Boolean merge, IssueType taskType) {
    return taskTypeService.updateTaskType(id, merge, taskType);
  }

  /** 删除任务类型 */
  public Boolean removeIssueType(Long id) {
    taskTypeService.removeTaskType(id);
    return true;
  }

  /** 新建任务类型方案 */
  public IssueTypeScheme createIssueTypeScheme(IssueTypeScheme issueTypeScheme) {
    return issueTypeSchemeService.createIssueTypeScheme(issueTypeScheme);
  }

  /** 编辑任务类型方案 */
  public IssueTypeScheme updateIssueTypeScheme(
      Long id, Boolean merge, IssueTypeScheme issueTypeScheme) {
    return issueTypeSchemeService.updateIssueTypeScheme(id, merge, issueTypeScheme);
  }

  /** 删除任务类型方案 */
  public Boolean removeIssueTypeScheme(Long id) {
    return issueTypeSchemeService.removeIssueTypeScheme(id);
  }
}
