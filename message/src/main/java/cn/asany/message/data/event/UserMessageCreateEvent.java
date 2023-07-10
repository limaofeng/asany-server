package cn.asany.message.data.event;

import cn.asany.message.data.domain.UserMessage;
import java.time.Clock;
import org.springframework.context.ApplicationEvent;

/**
 * 创建用户消息事件
 *
 * @author limaofeng
 */
public class UserMessageCreateEvent extends ApplicationEvent {
  public UserMessageCreateEvent(UserMessage message) {
    super(message, parseClock(message));
  }

  private static Clock parseClock(UserMessage message) {
    Clock clock = Clock.systemDefaultZone();
    return Clock.fixed(message.getCreatedAt().toInstant(), clock.getZone());
  }
}
