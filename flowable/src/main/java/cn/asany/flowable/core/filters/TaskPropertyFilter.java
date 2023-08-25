package cn.asany.flowable.core.filters;

import java.util.Date;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.TaskInfoQuery;
import org.flowable.task.api.TaskQuery;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.jfantasy.framework.dao.MatchType;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.spring.SpringBeanUtils;

/**
 * 任务查询过滤器
 *
 * @author limaofeng
 */
public class TaskPropertyFilter
    extends PropertyFilterBuilder<TaskPropertyFilter, TaskInfoQuery<?, ?>> {
  private final boolean isHistory;

  protected TaskPropertyFilter(TaskInfoQuery taskQuery) {
    super(taskQuery);
    this.isHistory = taskQuery instanceof HistoricTaskInstanceQuery;
    this.custom(
        "owner",
        new MatchType[] {MatchType.EQ},
        (name, matchType, value, context) -> {
          if (matchType == MatchType.EQ) {
            context.taskOwner(value.toString());
          }
        });

    this.custom(
        "assignee",
        new MatchType[] {MatchType.EQ},
        (name, matchType, value, context) -> {
          if (matchType == MatchType.EQ) {
            context.taskAssignee(value.toString());
          }
        });

    this.custom(
        "unassigned",
        new MatchType[] {MatchType.EQ},
        (name, matchType, value, context) -> {
          if (!(Boolean) value) {
            return;
          }
          if (matchType == MatchType.EQ) {
            getTaskQuery().taskUnassigned();
          }
        });

    this.custom(
        "incomplete",
        new MatchType[] {MatchType.EQ},
        (name, matchType, value, context) -> {
          if (!isHistory || !(Boolean) value) {
            return;
          }
          if (matchType == MatchType.EQ) {
            getHistoricTaskInstanceQuery().unfinished();
          }
        });

    this.custom(
        "completed",
        new MatchType[] {MatchType.EQ},
        (name, matchType, value, context) -> {
          if (!isHistory || !(Boolean) value) {
            return;
          }
          if (matchType == MatchType.EQ) {
            getHistoricTaskInstanceQuery().finished();
          }
        });

    this.custom(
        "completedAfter",
        new MatchType[] {MatchType.EQ},
        (name, matchType, value, context) -> {
          if (matchType == MatchType.EQ) {
            getHistoricTaskInstanceQuery().taskCompletedAfter((Date) value);
          }
        });

    this.custom(
        "completedOn",
        new MatchType[] {MatchType.EQ},
        (name, matchType, value, context) -> {
          if (matchType == MatchType.EQ) {
            getHistoricTaskInstanceQuery().taskCompletedOn((Date) value);
          }
        });

    this.custom(
        "completedBefore",
        new MatchType[] {MatchType.EQ},
        (name, matchType, value, context) -> {
          if (matchType == MatchType.EQ) {
            getHistoricTaskInstanceQuery().taskCompletedBefore((Date) value);
          }
        });

    this.custom(
        "category",
        new MatchType[] {MatchType.EQ},
        (name, matchType, value, context) -> {
          if (matchType == MatchType.EQ) {
            context.taskCategory(value.toString());
          }
        });
  }

  public static TaskPropertyFilter newFilter() {
    return newFilter(false);
  }

  public static TaskPropertyFilter newFilter(boolean historic) {
    TaskInfoQuery<?, ?> taskQuery =
        historic
            ? SpringBeanUtils.getBean(HistoryService.class).createHistoricTaskInstanceQuery()
            : SpringBeanUtils.getBean(TaskService.class).createTaskQuery();
    return new TaskPropertyFilter(taskQuery);
  }

  @Override
  public TaskInfoQuery build() {
    return this.context;
  }

  public HistoricTaskInstanceQuery getHistoricTaskInstanceQuery() {
    return (HistoricTaskInstanceQuery) this.context;
  }

  public TaskQuery getTaskQuery() {
    return (TaskQuery) this.context;
  }
}
