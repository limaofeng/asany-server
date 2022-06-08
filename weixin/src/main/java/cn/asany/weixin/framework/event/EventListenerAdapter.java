package cn.asany.weixin.framework.event;

import cn.asany.weixin.framework.message.EventMessage;
import cn.asany.weixin.framework.message.content.Event;
import cn.asany.weixin.framework.message.content.EventLocation;
import cn.asany.weixin.framework.session.WeixinSession;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EventListenerAdapter {

  private final List<WeixinEventListener> listeners;
  private final EventMessage.EventType eventType;

  public EventListenerAdapter(
      EventMessage.EventType eventType, List<WeixinEventListener> listeners) {
    this.eventType = eventType;
    this.listeners = listeners;
  }

  public void execute(WeixinSession session, Event content, EventMessage<?> message) {
    for (WeixinEventListener listener : listeners) {
      if (listener instanceof ClickEventListener && eventType == EventMessage.EventType.CLICK) {
        ((ClickEventListener) listener).onClick(session, content, message);
      } else if (listener instanceof LocationEventListener
          && eventType == EventMessage.EventType.location) {
        ((LocationEventListener) listener).onLocation(session, (EventLocation) content, message);
      } else if (listener instanceof ScanEventListener
          && eventType == EventMessage.EventType.SCAN) {
        ((ScanEventListener) listener).onScan(session, content, message);
      } else if (listener instanceof SubscribeEventListener
          && eventType == EventMessage.EventType.subscribe) {
        ((SubscribeEventListener) listener).onSubscribe(session, content, message);
      } else if (listener instanceof UnsubscribeEventListener
          && eventType == EventMessage.EventType.unsubscribe) {
        ((UnsubscribeEventListener) listener).onUnsubscribe(session, content, message);
      } else if (listener instanceof ViewEventListener
          && eventType == EventMessage.EventType.VIEW) {
        ((ViewEventListener) listener).onView(session, content, message);
      } else {
        log.error("监听器的与监听类型不匹配:" + eventType + "=" + listener);
      }
    }
  }
}
