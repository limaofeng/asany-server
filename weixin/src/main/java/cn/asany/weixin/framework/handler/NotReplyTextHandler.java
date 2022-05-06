package cn.asany.weixin.framework.handler;

import cn.asany.weixin.framework.message.EmptyMessage;
import cn.asany.weixin.framework.message.TextMessage;
import cn.asany.weixin.framework.message.WeixinMessage;
import cn.asany.weixin.framework.session.WeixinSession;

public class NotReplyTextHandler extends TextWeixinHandler {

  @Override
  protected WeixinMessage handleTextMessage(WeixinSession session, TextMessage message) {
    return EmptyMessage.get();
  }
}
