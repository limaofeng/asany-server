package cn.asany.weixin.framework.factory;

import cn.asany.weixin.framework.exception.NoSessionException;
import cn.asany.weixin.framework.exception.WeixinException;
import cn.asany.weixin.framework.session.WeixinSession;

/**
 * 微信会话
 *
 * @author limaofeng
 */
public class WeixinSessionUtils {

  /** 当前 session 对象 */
  private static final ThreadLocal<WeixinSession> CURRENT = new ThreadLocal<>();

  private WeixinSessionUtils() {
    throw new IllegalStateException("Utility class");
  }

  public static WeixinSession getCurrentSession() throws WeixinException {
    if (CURRENT.get() == null) {
      throw new NoSessionException("未初始化 WeiXinSession 对象");
    }
    return CURRENT.get();
  }

  public static WeixinSession saveSession(WeixinSession session) {
    CURRENT.set(session);
    return session;
  }

  public static void closeSession() {
    CURRENT.remove();
  }
}
