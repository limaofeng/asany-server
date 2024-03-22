package cn.asany.weixin.framework.factory;

import cn.asany.weixin.event.WeixinEventMessageEvent;
import cn.asany.weixin.event.WeixinMessageEvent;
import cn.asany.weixin.framework.account.WeixinAppService;
import cn.asany.weixin.framework.core.WeixinCoreHelper;
import cn.asany.weixin.framework.event.WeixinEventListener;
import cn.asany.weixin.framework.exception.WeixinException;
import cn.asany.weixin.framework.handler.WeixinHandler;
import cn.asany.weixin.framework.intercept.DefaultInvocation;
import cn.asany.weixin.framework.intercept.WeixinEventMessageInterceptor;
import cn.asany.weixin.framework.intercept.WeixinMessageInterceptor;
import cn.asany.weixin.framework.message.EventMessage;
import cn.asany.weixin.framework.message.WeixinMessage;
import cn.asany.weixin.framework.session.DefaultWeixinSession;
import cn.asany.weixin.framework.session.WeixinApp;
import cn.asany.weixin.framework.session.WeixinSession;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.Getter;
import lombok.Setter;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.context.ApplicationContext;

/**
 * 微信会话工厂默认实现
 *
 * @author limaofeng
 */
public class DefaultWeixinSessionFactory implements WeixinSessionFactory {

  @Setter @Getter private WeixinCoreHelper weixinCoreHelper;

  @Setter @Getter private WeixinAppService weixinAppService;

  private final ApplicationContext applicationContext;

  @Setter @Getter private Class<? extends WeixinSession> sessionClass = DefaultWeixinSession.class;

  @Setter private WeixinHandler messageHandler;

  @Setter private WeixinHandler eventHandler;

  @Setter private List<WeixinMessageInterceptor> weixinMessageInterceptors = new ArrayList<>();

  private final Map<EventMessage.EventType, List<WeixinEventListener>> eventListeners =
      new EnumMap<>(EventMessage.EventType.class);

  private final ConcurrentMap<String, WeixinSession> weiXinSessions = new ConcurrentHashMap<>();

  public DefaultWeixinSessionFactory(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  @Override
  public WeixinSession getCurrentSession() throws WeixinException {
    return WeixinSessionUtils.getCurrentSession();
  }

  @Override
  public WeixinSession openSession(String appid) throws WeixinException {
    if (!weiXinSessions.containsKey(appid)) {
      WeixinApp weixinApp = this.weixinAppService.loadAccountByAppid(appid);
      this.weixinCoreHelper.register(weixinApp);
      weiXinSessions.put(appid, new DefaultWeixinSession(weixinApp, weixinCoreHelper));
    }
    return weiXinSessions.get(appid);
  }

  @Override
  public WeixinMessage<?> execute(WeixinMessage<?> message) throws WeixinException {
    List<Object> handler = new ArrayList<>(weixinMessageInterceptors);
    if (message instanceof EventMessage) {
      applicationContext.publishEvent(
          new WeixinEventMessageEvent((EventMessage<?>) message)); // 事件推送
      final List<WeixinEventListener> listeners =
          ObjectUtil.defaultValue(
              eventListeners.get(((EventMessage<?>) message).getEventType()),
              Collections.<WeixinEventListener>emptyList());
      handler.add(
          new WeixinEventMessageInterceptor(
              ((EventMessage<?>) message).getEventType(), listeners)); // 添加Event拦截器,用于触发事件
      handler.add(this.eventHandler);
    } else {
      applicationContext.publishEvent(new WeixinMessageEvent(message)); // 消息事件推送
      handler.add(this.messageHandler);
    }
    return new DefaultInvocation(
            WeixinSessionUtils.getCurrentSession(), message, handler.iterator())
        .invoke();
  }

  public void setEventListeners(
      Map<EventMessage.EventType, List<WeixinEventListener>> eventListeners) {
    this.eventListeners.putAll(eventListeners);
  }
}
