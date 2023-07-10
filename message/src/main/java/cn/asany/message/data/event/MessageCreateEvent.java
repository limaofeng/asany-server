package cn.asany.message.data.event;

import cn.asany.message.data.domain.Message;
import java.time.Clock;
import org.springframework.context.ApplicationEvent;

/**
 * 创建消息事件
 *
 * @author limaofeng
 */
public class MessageCreateEvent extends ApplicationEvent {
  public MessageCreateEvent(Message message) {
    super(message, parseClock(message));
  }

  private static Clock parseClock(Message message) {
    Clock clock = Clock.systemDefaultZone();
    return Clock.fixed(message.getCreatedAt().toInstant(), clock.getZone());
  }
}
