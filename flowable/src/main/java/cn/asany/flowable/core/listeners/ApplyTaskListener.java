package cn.asany.flowable.core.listeners;

import org.flowable.engine.delegate.TaskListener;
import org.flowable.task.service.delegate.DelegateTask;
import org.springframework.stereotype.Component;

@Component
public class ApplyTaskListener implements TaskListener {

  public ApplyTaskListener() {
    System.out.println("ApplyTaskListener");
  }

  @Override
  public void notify(DelegateTask delegateTask) {
    String assignee = (String) delegateTask.getVariable("assignee");
    // delegateTask.setAssignee(assignee); // 历史表中的assignee还是为null
    // taskService.setAssignee(delegateTask.getId(), startUserId);
    System.out.println(assignee);
  }
}
