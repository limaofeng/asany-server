package cn.asany.weixin.framework.intercept;

import cn.asany.weixin.framework.exception.WeixinException;
import cn.asany.weixin.framework.handler.WeixinHandler;
import cn.asany.weixin.framework.message.WeixinMessage;
import cn.asany.weixin.framework.session.WeixinSession;
import java.util.Iterator;

/** 调用者 */
public class DefaultInvocation implements Invocation {

  private Object handler;
  private WeixinSession session;
  private WeixinMessage message;
  private Iterator<?> iterator;

  public DefaultInvocation(WeixinSession session, WeixinMessage message, Iterator<?> iterator) {
    this.session = session;
    this.message = message;
    this.handler = iterator.next();
    this.iterator = iterator;
  }

  @Override
  public WeixinMessage invoke() throws WeixinException {
    if (this.handler instanceof WeixinHandler) {
      return ((WeixinHandler) this.handler).handleMessage(this.session, message);
    } else if (this.handler instanceof WeixinMessageInterceptor && this.iterator.hasNext()) {
      return ((WeixinMessageInterceptor) this.handler)
          .intercept(this.session, message, new DefaultInvocation(session, message, this.iterator));
    } else {
      throw new WeixinException("不能处理的 handler 类型 = " + handler.getClass());
    }
  }
}
