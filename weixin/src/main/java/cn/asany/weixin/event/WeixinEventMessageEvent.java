package cn.asany.weixin.event;

import cn.asany.weixin.framework.message.EventMessage;
import org.springframework.context.ApplicationEvent;

/** 微信事件消息事件(spring事件机制) */
public class WeixinEventMessageEvent extends ApplicationEvent {

  public WeixinEventMessageEvent(EventMessage message) {
    super(message);
  }
}
