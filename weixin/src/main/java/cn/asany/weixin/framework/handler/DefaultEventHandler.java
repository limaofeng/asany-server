package cn.asany.weixin.framework.handler;

import cn.asany.weixin.framework.message.EmptyMessage;
import cn.asany.weixin.framework.message.EventMessage;
import cn.asany.weixin.framework.message.WeixinMessage;
import cn.asany.weixin.framework.session.WeixinSession;

public class DefaultEventHandler extends EventWeixinHandler {

  @Override
  protected WeixinMessage handleEventMessage(WeixinSession session, EventMessage message) {
    return EmptyMessage.get();
  }
}
