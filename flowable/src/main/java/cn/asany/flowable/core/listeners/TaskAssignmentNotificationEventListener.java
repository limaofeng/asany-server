package cn.asany.flowable.core.listeners;

import cn.asany.message.api.MessageService;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.flowable.common.engine.api.delegate.event.FlowableEvent;
import org.flowable.common.engine.api.delegate.event.FlowableEventListener;
import org.flowable.common.engine.impl.cfg.TransactionState;
import org.flowable.engine.delegate.event.impl.FlowableEntityEventImpl;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.task.service.impl.persistence.entity.TaskEntity;

/**
 * 任务分配通知事件监听器
 *
 * @author limaofeng
 */
@Slf4j
public class TaskAssignmentNotificationEventListener implements FlowableEventListener {

  private final MessageService messageService;

  public TaskAssignmentNotificationEventListener(
      MessageService messageService) {
    this.messageService = messageService;
    if (this.messageService == null) {
      log.warn("MessageService is null");
    }
  }

  @Override
  public void onEvent(FlowableEvent flowableEvent) {
    // 获取节点内容
    TaskEntity taskEntity = (TaskEntity) ((FlowableEntityEventImpl) flowableEvent).getEntity();
    // 获取审批候选人（即谁来审批）
    Set<IdentityLink> candidates = taskEntity.getCandidates();
    // 获取流程名称
    String currentProcess = taskEntity.getName();
    // TODO 你的业务逻辑
    Map<String, Object> params = new HashMap<>(10);
    this.messageService.send("task-assignment-notification", params);
  }

  @Override
  public boolean isFailOnException() {
    return false;
  }

  @Override
  public boolean isFireOnTransactionLifecycleEvent() {
    return false;
  }

  @Override
  public String getOnTransaction() {
    // 事务提交后触发
    return TransactionState.COMMITTED.name();
  }
}
