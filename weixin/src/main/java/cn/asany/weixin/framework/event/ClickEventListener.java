package cn.asany.weixin.framework.event;

import cn.asany.weixin.framework.message.EventMessage;
import cn.asany.weixin.framework.message.content.Event;
import cn.asany.weixin.framework.session.WeixinSession;

/** 点击菜单拉取消息时的事件推送监听接口 */
public interface ClickEventListener extends WeixinEventListener {

  void onClick(WeixinSession session, Event event, EventMessage message);
}
