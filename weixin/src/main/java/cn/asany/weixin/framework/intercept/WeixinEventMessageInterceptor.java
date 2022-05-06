package cn.asany.weixin.framework.intercept;

import cn.asany.weixin.framework.event.EventListenerAdapter;
import cn.asany.weixin.framework.event.WeixinEventListener;
import cn.asany.weixin.framework.exception.WeixinException;
import cn.asany.weixin.framework.message.EventMessage;
import cn.asany.weixin.framework.message.WeixinMessage;
import cn.asany.weixin.framework.message.content.Event;
import cn.asany.weixin.framework.session.WeixinSession;
import java.util.List;

public class WeixinEventMessageInterceptor implements WeixinMessageInterceptor {

  private EventListenerAdapter adapter;

  public WeixinEventMessageInterceptor(
      EventMessage.EventType eventType, List<WeixinEventListener> listeners) {
    this.adapter = new EventListenerAdapter(eventType, listeners);
  }

  @Override
  public WeixinMessage intercept(
      WeixinSession session, WeixinMessage message, Invocation invocation) throws WeixinException {
    try {
      return invocation.invoke();
    } finally {
      adapter.execute(session, (Event) message.getContent(), (EventMessage) message);
    }
  }
}
