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
    this.property(
        "owner",
        new MatchType[] {MatchType.EQ},
        (name, matchType, value, context) -> {
          if (matchType == MatchType.EQ) {
            context.taskOwner(value.toString());
          }
        });

    this.property(
        "assignee",
        new MatchType[] {MatchType.EQ},
        (name, matchType, value, context) -> {
          if (matchType == MatchType.EQ) {
            context.taskAssignee(value.toString());
          }
        });

    this.property(
        "unassigned",
        new MatchType[] {MatchType.EQ},
        (name, matchType, value, context) -> {
          if (!(Boolean) value) {
            return;
          }
          if (matchType == MatchType.EQ) {
            if (((Boolean) value)) {
              getTaskQuery().taskUnassigned();
            }
          }
        });

    this.property(
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

    this.property(
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

    this.property(
        "completedAfter",
        new MatchType[] {MatchType.EQ},
        (name, matchType, value, context) -> {
          if (matchType == MatchType.EQ) {
            getHistoricTaskInstanceQuery().taskCompletedAfter((Date) value);
          }
        });

    this.property(
        "completedOn",
        new MatchType[] {MatchType.EQ},
        (name, matchType, value, context) -> {
          if (matchType == MatchType.EQ) {
            getHistoricTaskInstanceQuery().taskCompletedOn((Date) value);
          }
        });

    this.property(
        "completedBefore",
        new MatchType[] {MatchType.EQ},
        (name, matchType, value, context) -> {
          if (matchType == MatchType.EQ) {
            getHistoricTaskInstanceQuery().taskCompletedBefore((Date) value);
          }
        });

    this.property(
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
