package cn.asany.message.core;

import cn.asany.message.api.MessageException;
import cn.asany.message.api.MessageSender;
import cn.asany.message.api.SimpleMessage;
import cn.asany.message.data.graphql.subscription.NotificationPublisher;
import org.springframework.stereotype.Component;

/**
 * WebPush 消息发送者
 *
 * @author limaofeng
 */
@Component
public class WebPushMessageSender implements MessageSender {

  private final NotificationPublisher notificationPublisher;

  public WebPushMessageSender(NotificationPublisher notificationPublisher) {
    this.notificationPublisher = notificationPublisher;
  }

  @Override
  public void send(SimpleMessage simpleMessage) {
    notificationPublisher.emit(
        Notification.builder()
            .title(simpleMessage.getSubject())
            .content(simpleMessage.getText())
            .build());
  }
}
