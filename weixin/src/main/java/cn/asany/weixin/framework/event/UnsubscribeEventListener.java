package cn.asany.weixin.framework.event;

import cn.asany.weixin.framework.message.EventMessage;
import cn.asany.weixin.framework.message.content.Event;
import cn.asany.weixin.framework.session.WeixinSession;

/** 取消订阅事件消息监听接口 */
public interface UnsubscribeEventListener extends WeixinEventListener {

  void onUnsubscribe(WeixinSession session, Event event, EventMessage message);
}
