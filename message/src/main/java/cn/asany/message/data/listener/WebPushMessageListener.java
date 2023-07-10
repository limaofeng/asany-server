package cn.asany.message.data.listener;

import cn.asany.message.core.Notification;
import cn.asany.message.data.domain.UserMessage;
import cn.asany.message.data.event.UserMessageCreateEvent;
import cn.asany.message.data.graphql.subscription.NotificationPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * WebPush 消息监听器
 *
 * @author limaofeng
 */
@Component("listener.WebPushMessageListener")
public class WebPushMessageListener implements ApplicationListener<UserMessageCreateEvent> {

  private final NotificationPublisher notificationPublisher;

  public WebPushMessageListener(NotificationPublisher notificationPublisher) {
    this.notificationPublisher = notificationPublisher;
  }

  @Override
  public void onApplicationEvent(UserMessageCreateEvent event) {
    UserMessage message = (UserMessage) event.getSource();
    notificationPublisher.emit(
        Notification.builder()
            .id(message.getId())
            .type(message.getType())
            .userId(message.getUser().getId())
            .title(message.getTitle())
            .message(message.getContent())
            .uri(message.getUri())
            .build());
  }
}
