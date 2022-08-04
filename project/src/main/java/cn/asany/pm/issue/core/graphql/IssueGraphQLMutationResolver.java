package cn.asany.pm.issue.core.graphql;

import cn.asany.pm.field.graphql.model.IssueFieldValueInput;
import cn.asany.pm.issue.core.convert.IssueConverter;
import cn.asany.pm.issue.core.domain.Comment;
import cn.asany.pm.issue.core.domain.Follow;
import cn.asany.pm.issue.core.domain.Issue;
import cn.asany.pm.issue.core.domain.Worklog;
import cn.asany.pm.issue.core.graphql.input.IssueCreateInput;
import cn.asany.pm.issue.core.graphql.input.IssueUpdateInput;
import cn.asany.pm.issue.core.service.CommentService;
import cn.asany.pm.issue.core.service.FollowService;
import cn.asany.pm.issue.core.service.IssueService;
import cn.asany.pm.issue.core.service.WorklogService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Component
public class IssueGraphQLMutationResolver implements GraphQLMutationResolver {

  private final IssueService issueService;

  private final FollowService followService;

  private final WorklogService worklogService;

  private final CommentService commentService;

  private IssueConverter issueConverter;

  public IssueGraphQLMutationResolver(
      IssueService issueService,
      FollowService followService,
      WorklogService worklogService,
      CommentService commentService) {
    this.issueService = issueService;
    this.followService = followService;
    this.worklogService = worklogService;
    this.commentService = commentService;
  }

  /** 新增任务 */
  public Issue createIssue(IssueCreateInput input) {
    Issue issue = issueConverter.toIssue(input);
    return issueService.save(issue);
  }

  /** 更新任务 */
  public Issue updateIssue(Long id, IssueUpdateInput input, Boolean merge) {
    Issue issue = issueConverter.toIssue(input);
    return issueService.update(id, issue, merge);
  }

  /**
   * 保存字段的值
   *
   * @param issue 工单的id
   * @param action 操作的id
   */
  public Issue issueAction(Long issue, Long action, List<IssueFieldValueInput> values) {
    return issueService.issueAction(issue, action, values);
  }

  /** 关注任务 */
  public Boolean watchIssue(Long issue, Long uid) {
    return followService.watchIssue(issue, Follow.builder().uid(uid).build());
  }

  /** 取消关注 */
  public Boolean unwatchIssue(Long issueFollow) {
    return followService.unwatchIssue(issueFollow);
  }

  /** 增加任务日志 */
  public Worklog createIssueWorkLog(Long issue, Worklog issueTaskLog) {
    return worklogService.save(issue, issueTaskLog);
  }

  /** 增加任务注释 */
  public Comment createIssueComment(Long issue, Comment comment) {
    return commentService.save(issue, comment);
  }
  /** 增加任务结果 */
  public Boolean resolutionIssue(Long issue, Long resolutionId) {
    return issueService.resolutionIssue(issue, resolutionId);
  }

  /** 开始任务 */
  public Boolean startIssue(Long issue) {
    return issueService.startIssue(issue);
  }

  /** 暂停任务任务 */
  public Boolean pauseIssue(Long issue, String content) {
    return issueService.pauseIssue(issue, content);
  }
}
