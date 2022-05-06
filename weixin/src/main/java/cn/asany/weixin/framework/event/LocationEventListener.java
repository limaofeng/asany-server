package cn.asany.weixin.framework.event;

import cn.asany.weixin.framework.message.EventMessage;
import cn.asany.weixin.framework.message.content.EventLocation;
import cn.asany.weixin.framework.session.WeixinSession;

/** 微信事件消息监听接口 */
public interface LocationEventListener extends WeixinEventListener {

  void onLocation(WeixinSession session, EventLocation event, EventMessage message);
}
