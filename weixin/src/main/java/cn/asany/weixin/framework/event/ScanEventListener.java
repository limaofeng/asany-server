package cn.asany.weixin.framework.event;

import cn.asany.weixin.framework.message.EventMessage;
import cn.asany.weixin.framework.message.content.Event;
import cn.asany.weixin.framework.session.WeixinSession;

/** 用户已关注时的事件推送监听接口 */
public interface ScanEventListener extends WeixinEventListener {

  void onScan(WeixinSession session, Event event, EventMessage message);
}
