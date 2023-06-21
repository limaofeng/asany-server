package cn.asany.flowable.core.service;

import cn.asany.flowable.core.domain.PersonalTask;
import cn.asany.flowable.core.filters.TaskStatus;
import java.util.ArrayList;
import java.util.List;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.task.Comment;
import org.flowable.idm.api.User;
import org.flowable.task.api.*;
import org.flowable.task.service.impl.HistoricTaskInstanceQueryProperty;
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
public class TaskInfoService {

  private static final String CATEGORY_PERSONAL_TASK = "PersonalTask";

  private final TaskService taskService;

  private final HistoryService historyService;

  public TaskInfoService(TaskService taskService, HistoryService historyService) {
    this.taskService = taskService;
    this.historyService = historyService;
  }

  public Page<TaskInfo> findPage(Pageable page, PropertyFilter filter) {
    TaskInfoQuery<?, ?> query = filter.build();

    if (page.getSort().isSorted()) {
      query.orderBy(HistoricTaskInstanceQueryProperty.findByName("")).desc();
    }

    List<TaskInfo> list =
        new ArrayList<>(
            query.listPage(Long.valueOf(page.getOffset()).intValue(), page.getPageSize()));
    return new PageImpl<>(list, page, query.count());
  }

  /**
   * 任务签收 / 任务分配
   *
   * @param taskId 任务ID
   * @param userId 用户ID
   */
  public void assigneeTask(String taskId, Long userId) {
    taskService.setAssignee(taskId, String.valueOf(userId));
  }

  /**
   * 完成任务
   *
   * @param taskId 任务ID
   */
  public void completeTask(String taskId) {
    taskService.deleteTask(taskId, TaskStatus.COMPLETED.getValue());
  }

  /**
   * 创建个人任务
   *
   * @param task 任务信息
   * @param createdBy 创建人
   * @return TaskInfo
   */
  public TaskInfo createPersonalTask(PersonalTask task, User createdBy) {
    TaskBuilder taskBuilder =
        taskService
            .createTaskBuilder()
            .owner(createdBy.getId())
            .assignee(createdBy.getId())
            .name(task.getName())
            .description(task.getDescription())
            .dueDate(task.getDueDate())
            .category(CATEGORY_PERSONAL_TASK);
    if (task.getPriority() != null) {
      taskBuilder.priority(task.getPriority());
    }
    return taskBuilder.create();
  }

  /**
   * 添加评论
   *
   * @param taskId 任务ID
   * @param message 评论内容
   */
  public void addComment(String taskId, String message) {
    taskService.addComment(taskId, null, message);
  }

  /**
   * 删除评论
   *
   * @param commentId 评论ID
   */
  public void deleteComment(String commentId) {
    taskService.deleteComment(commentId);
  }

  /**
   * 删除任务
   *
   * @param taskId 任务ID
   */
  public void deleteTask(String taskId) {
    taskService.deleteTask(taskId);
  }

  /**
   * 获取任务评论
   *
   * @param taskId 任务ID
   * @param type 评论类型
   * @return List<Comment> 评论列表
   */
  public List<Comment> getTaskComments(String taskId, String type) {
    return taskService.getTaskComments(taskId, type);
  }

  /**
   * 获取任务评论
   *
   * @param taskId 任务ID
   * @return List<Comment> 评论列表
   */
  public TaskInfo getTask(String taskId) {
    return historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
  }

  public Task updateTask(PersonalTask task) {
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
    return null;
  }
}
