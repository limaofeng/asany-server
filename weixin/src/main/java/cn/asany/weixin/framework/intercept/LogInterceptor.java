package cn.asany.weixin.framework.intercept;

import cn.asany.weixin.framework.exception.WeixinException;
import cn.asany.weixin.framework.message.WeixinMessage;
import cn.asany.weixin.framework.session.WeixinSession;
import lombok.extern.slf4j.Slf4j;

/** 记录接收及发生的消息 */
@Slf4j
public class LogInterceptor implements WeixinMessageInterceptor {

  @Override
  public WeixinMessage intercept(
      WeixinSession session, WeixinMessage message, Invocation invocation) throws WeixinException {

    log.debug("接收到的消息内容:" + message.getContent());

    WeixinMessage outMessage = invocation.invoke();

    log.debug("回复的消息内容:" + (outMessage == null ? "null" : outMessage.getContent()));

    return outMessage;
  }
}
