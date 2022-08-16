package cn.asany.sms.event;

import cn.asany.sms.domain.ShortMessage;
import org.springframework.context.ApplicationEvent;

public class SendMessageEvent extends ApplicationEvent {

  public SendMessageEvent(ShortMessage message) {
    super(message);
  }

  public ShortMessage getMessage() {
    return (ShortMessage) this.getSource();
  }
}
