package cn.asany.weixin.framework.intercept;

import cn.asany.weixin.framework.exception.WeixinException;
import cn.asany.weixin.framework.message.WeixinMessage;
import cn.asany.weixin.framework.session.WeixinSession;

/** 微信消息拦截器 */
public interface WeixinMessageInterceptor {

  /**
   * 消息拦截器
   *
   * @param session 微信公众号
   * @param message 消息
   * @param invocation 调用
   * @return WeiXinMessage
   */
  WeixinMessage intercept(WeixinSession session, WeixinMessage message, Invocation invocation)
      throws WeixinException;
}
