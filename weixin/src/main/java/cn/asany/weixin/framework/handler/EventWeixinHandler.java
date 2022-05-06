package cn.asany.weixin.framework.handler;

import cn.asany.weixin.framework.message.*;
import cn.asany.weixin.framework.session.WeixinSession;

/** 事件消息处理器 */
public abstract class EventWeixinHandler extends AbstracWeixinHandler {

  @Override
  protected WeixinMessage handleImageMessage(WeixinSession session, ImageMessage message) {
    return EmptyMessage.get();
  }

  @Override
  protected WeixinMessage handleVideoMessage(WeixinSession session, VideoMessage message) {
    return EmptyMessage.get();
  }

  @Override
  protected WeixinMessage handleLocationMessage(WeixinSession session, LocationMessage message) {
    return EmptyMessage.get();
  }

  @Override
  protected WeixinMessage handleVoiceMessage(WeixinSession session, VoiceMessage message) {
    return EmptyMessage.get();
  }

  @Override
  protected WeixinMessage handleLinkMessage(WeixinSession session, LinkMessage message) {
    return EmptyMessage.get();
  }

  @Override
  protected WeixinMessage handleTextMessage(WeixinSession session, TextMessage message) {
    return EmptyMessage.get();
  }
}
