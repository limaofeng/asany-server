package cn.asany.flowable.core.service;

import java.util.List;
import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.springframework.stereotype.Service;

@Service
public class PersonalTaskService {

  private final TaskService taskService;

  private final HistoryService historyService;

  public PersonalTaskService(TaskService taskService, HistoryService historyService) {
    this.taskService = taskService;
    this.historyService = historyService;
  }

  public void createTask() {
    LoginUser user = SpringSecurityUtils.getCurrentUser();

    Task task =
        taskService
            .createTaskBuilder()
            .owner(String.valueOf(user.getUid()))
            .assignee(String.valueOf(user.getUid()))
            .name("EventName")
            .description("Description")
            .category("PersonalTask")
            .create();

    List<HistoricTaskInstance> tasks =
        historyService.createHistoricTaskInstanceQuery().taskId(task.getId()).list();

    tasks.get(0).getStartTime();

    // TODO 修改历史表中的 开始与结束 时间

    System.out.println("task id : " + task.getId());
  }
}
