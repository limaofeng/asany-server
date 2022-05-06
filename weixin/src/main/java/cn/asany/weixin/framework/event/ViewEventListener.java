package cn.asany.weixin.framework.event;

import cn.asany.weixin.framework.message.EventMessage;
import cn.asany.weixin.framework.message.content.Event;
import cn.asany.weixin.framework.session.WeixinSession;

/** 点击菜单跳转链接时的事件推送监听接口 */
public interface ViewEventListener extends WeixinEventListener {

  void onView(WeixinSession session, Event event, EventMessage message);
}
