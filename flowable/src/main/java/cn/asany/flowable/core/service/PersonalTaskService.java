package cn.asany.flowable.core.service;

import cn.asany.flowable.core.domain.PersonalTask;
import java.util.ArrayList;
import java.util.List;

import cn.asany.flowable.core.filters.TaskPropertyFilter;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.TaskBuilder;
import org.flowable.task.api.TaskInfo;
import org.flowable.task.api.TaskQuery;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 个人任务
 *
 * @author limaofeng
 */
@Service
public class PersonalTaskService {

  private static final String CATEGORY_PERSONAL_TASK = "PersonalTask";

  private final TaskService taskService;

  private final HistoryService historyService;

  public PersonalTaskService(TaskService taskService, HistoryService historyService) {
    this.taskService = taskService;
    this.historyService = historyService;
  }

  public Page<TaskInfo> findPage(Pageable page, TaskPropertyFilter filter) {
//    TaskQuery query = taskService.createTaskQuery();
    TaskQuery query = filter.build();
//    for (PropertyFilter filter : filters) {
//      if ("owner".equals(filter.getPropertyName())) {
//        if (filter.getMatchType() == PropertyFilter.MatchType.EQ) {
//          query.taskOwner(filter.getPropertyValue().toString());
//        }
//      }
//    }

    List<TaskInfo> list =
        new ArrayList<>(
            query.listPage(Long.valueOf(page.getOffset()).intValue(), page.getPageSize()));
    return new PageImpl<>(list, page, query.count());
  }

  public void completeTask(String taskId) {
    taskService.complete(taskId);
  }

  public TaskInfo createTask(PersonalTask task) {
    TaskBuilder taskBuilder =
        taskService
            .createTaskBuilder()
            .owner(String.valueOf(task.getUid()))
            .assignee(String.valueOf(task.getUid()))
            .name(task.getName())
            .description(task.getDescription())
            .dueDate(task.getDueDate())
            .category(CATEGORY_PERSONAL_TASK);

    if (task.getPriority() != null) {
      taskBuilder.priority(task.getPriority());
    }

    Task personalTask = taskBuilder.create();

    //    List<HistoricTaskInstance> tasks =
    //        historyService.createHistoricTaskInstanceQuery().taskId(personalTask.getId()).list();
    //
    //    tasks.get(0).getStartTime();

    // TODO 修改历史表中的 开始与结束 时间

    //    System.out.println("task id : " + tasks.get(0).getStartTime());
    return personalTask;
  }

  public void addComment(String taskId, String message) {
    taskService.addComment(taskId, null, message);
  }

  public void deleteComment(String commentId) {
    taskService.deleteComment(commentId);
  }

  public void deleteTask(String taskId) {
    taskService.deleteTask(taskId);
  }

  public TaskInfo getTask(String taskId) {
    return historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
  }

  //  public Task updateTask(PersonalTask task) {
  //
  //      taskService.deleteTask();
  //
  //    if (task.getPriority() != null) {
  //      taskBuilder.priority(task.getPriority());
  //    }
  //
  //    Task personalTask = taskBuilder.create();
  //
  //    List<HistoricTaskInstance> tasks =
  //        historyService.createHistoricTaskInstanceQuery().taskId(personalTask.getId()).list();
  //
  //    tasks.get(0).getStartTime();
  //
  //    // TODO 修改历史表中的 开始与结束 时间
  //
  //    System.out.println("task id : " + tasks.get(0).getStartTime());
  //    return personalTask;
  //  }
}
