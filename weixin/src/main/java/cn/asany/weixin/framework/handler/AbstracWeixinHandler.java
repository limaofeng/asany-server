package cn.asany.weixin.framework.handler;

import cn.asany.weixin.framework.exception.WeixinException;
import cn.asany.weixin.framework.message.*;
import cn.asany.weixin.framework.session.WeixinSession;

/** 微信处理器 */
public abstract class AbstracWeixinHandler implements WeixinHandler {

  @Override
  public WeixinMessage<?> handleMessage(WeixinSession session, WeixinMessage<?> message)
      throws WeixinException {
    WeixinMessage outMessage;
    if (message instanceof TextMessage) {
      outMessage = handleTextMessage(session, (TextMessage) message);
    } else if (message instanceof ImageMessage) {
      outMessage = handleImageMessage(session, (ImageMessage) message);
    } else if (message instanceof VoiceMessage) {
      outMessage = handleVoiceMessage(session, (VoiceMessage) message);
    } else if (message instanceof VideoMessage) {
      outMessage = handleVideoMessage(session, (VideoMessage) message);
    } else if (message instanceof LocationMessage) {
      outMessage = handleLocationMessage(session, (LocationMessage) message);
    } else if (message instanceof LinkMessage) {
      outMessage = handleLinkMessage(session, (LinkMessage) message);
    } else if (message instanceof EventMessage) {
      outMessage = handleEventMessage(session, (EventMessage) message);
    } else {
      throw new IllegalStateException("Unexpected WeiXin message type: " + message);
    }
    if (outMessage == null || outMessage == EmptyMessage.get()) {
      return null;
    }
    if (!(outMessage instanceof AbstractWeixinMessage)) {
      return outMessage;
    }
    AbstractWeixinMessage absoutMessage = (AbstractWeixinMessage) outMessage;
    absoutMessage.setFromUserName(message.getToUserName());
    absoutMessage.setToUserName(message.getFromUserName());
    return absoutMessage;
  }

  protected abstract WeixinMessage handleEventMessage(WeixinSession session, EventMessage message);

  protected abstract WeixinMessage handleImageMessage(WeixinSession session, ImageMessage message);

  protected abstract WeixinMessage handleVideoMessage(WeixinSession session, VideoMessage message);

  protected abstract WeixinMessage handleLocationMessage(
      WeixinSession session, LocationMessage message);

  protected abstract WeixinMessage handleVoiceMessage(WeixinSession session, VoiceMessage message);

  protected abstract WeixinMessage handleLinkMessage(WeixinSession session, LinkMessage message);

  protected abstract WeixinMessage handleTextMessage(WeixinSession session, TextMessage message);
}
