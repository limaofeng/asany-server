package cn.asany.weixin.framework.handler;

import cn.asany.weixin.framework.exception.WeixinException;
import cn.asany.weixin.framework.message.WeixinMessage;
import cn.asany.weixin.framework.session.WeixinSession;

public interface WeixinHandler {

  /**
   * 消息处理类
   *
   * @param session 微信回话
   * @param message 微信消息
   * @throws WeixinException
   */
  WeixinMessage<?> handleMessage(WeixinSession session, WeixinMessage<?> message)
      throws WeixinException;
}
