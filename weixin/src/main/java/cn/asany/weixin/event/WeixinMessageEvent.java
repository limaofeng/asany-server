package cn.asany.weixin.event;

import cn.asany.weixin.framework.message.WeixinMessage;
import org.springframework.context.ApplicationEvent;

/** 微信消息事件(spring事件机制) */
public class WeixinMessageEvent extends ApplicationEvent {

  public WeixinMessageEvent(WeixinMessage message) {
    super(message);
  }
}
