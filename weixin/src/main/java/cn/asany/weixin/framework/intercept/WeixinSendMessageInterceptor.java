package cn.asany.weixin.framework.intercept;

import cn.asany.weixin.framework.exception.WeixinException;
import cn.asany.weixin.framework.session.WeixinSession;

/** 微信主动发送消息拦截器 */
public interface WeixinSendMessageInterceptor<T, E> {

  /**
   * 消息拦截器
   *
   * @param session 微信公众号
   * @param message 消息内容
   * @param to 接收人
   * @param invocation 回调处理
   * @throws WeixinException
   */
  void intercept(WeixinSession session, T message, E to, Invocation invocation)
      throws WeixinException;
}
