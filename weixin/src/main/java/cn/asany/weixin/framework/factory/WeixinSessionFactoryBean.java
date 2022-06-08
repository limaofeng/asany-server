package cn.asany.weixin.framework.factory;

import cn.asany.weixin.framework.account.WeixinAppService;
import cn.asany.weixin.framework.core.WeixinCoreHelper;
import cn.asany.weixin.framework.event.WeixinEventListener;
import cn.asany.weixin.framework.handler.DefaultEventHandler;
import cn.asany.weixin.framework.handler.NotReplyTextHandler;
import cn.asany.weixin.framework.handler.WeixinHandler;
import cn.asany.weixin.framework.intercept.LogInterceptor;
import cn.asany.weixin.framework.intercept.WeixinMessageInterceptor;
import cn.asany.weixin.framework.message.EventMessage;
import cn.asany.weixin.framework.session.DefaultWeixinSession;
import cn.asany.weixin.framework.session.WeixinSession;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;

/**
 * 微信会话工厂类
 *
 * @author limaofeng
 */
@Slf4j
public class WeixinSessionFactoryBean implements FactoryBean<WeixinSessionFactory> {

  private ApplicationContext applicationContext;

  /** 微信session工厂 */
  private WeixinSessionFactory weixinSessionFactory;

  private WeixinCoreHelper weixinCoreHelper;

  private WeixinAppService weixinAppService;

  private Class<? extends WeixinSession> sessionClass = DefaultWeixinSession.class;

  private WeixinHandler messageHandler = new NotReplyTextHandler();

  private WeixinHandler eventHandler = new DefaultEventHandler();

  private Map<EventMessage.EventType, List<WeixinEventListener>> eventListeners =
      new EnumMap<>(EventMessage.EventType.class);

  private List<WeixinMessageInterceptor> weixinMessageInterceptors = new ArrayList<>();

  public void afterPropertiesSet() {
    long start = System.currentTimeMillis();

    weixinMessageInterceptors.add(new LogInterceptor());

    DefaultWeixinSessionFactory factory = new DefaultWeixinSessionFactory(applicationContext);

    factory.setWeixinAppService(this.weixinAppService);

    factory.setWeixinCoreHelper(this.weixinCoreHelper);

    factory.setMessageHandler(this.messageHandler);
    factory.setEventHandler(this.eventHandler);
    factory.setSessionClass(sessionClass);
    factory.setWeixinMessageInterceptors(weixinMessageInterceptors);
    factory.setEventListeners(this.eventListeners);

    this.weixinSessionFactory = factory;

    log.debug("\n初始化 WeiXinSessionFactory 耗时:" + (System.currentTimeMillis() - start) + "ms");
  }

  @Override
  public WeixinSessionFactory getObject() {
    if (this.weixinSessionFactory == null) {
      afterPropertiesSet();
    }
    return weixinSessionFactory;
  }

  @Override
  public Class<?> getObjectType() {
    return DefaultWeixinSessionFactory.class;
  }

  @Override
  public boolean isSingleton() {
    return true;
  }

  public void setWeixinCoreHelper(WeixinCoreHelper weixinCoreHelper) {
    this.weixinCoreHelper = weixinCoreHelper;
  }

  public void setWeixinMessageInterceptors(
      List<WeixinMessageInterceptor> weixinMessageInterceptors) {
    this.weixinMessageInterceptors = weixinMessageInterceptors;
  }

  public void setMessageHandler(WeixinHandler messageHandler) {
    this.messageHandler = messageHandler;
  }

  public void setEventHandler(WeixinHandler eventHandler) {
    this.eventHandler = eventHandler;
  }

  public void setSessionClass(Class<? extends WeixinSession> sessionClass) {
    this.sessionClass = sessionClass;
  }

  public void setWeixinAppService(WeixinAppService weixinAppService) {
    this.weixinAppService = weixinAppService;
  }

  public void setEventListeners(
      Map<EventMessage.EventType, List<WeixinEventListener>> eventListeners) {
    this.eventListeners = eventListeners;
  }

  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  public void addEventListener(
      EventMessage.EventType eventType, WeixinEventListener weixinEventListener) {
    if (!this.eventListeners.containsKey(eventType)) {
      this.eventListeners.put(eventType, new ArrayList<>());
    }
    this.eventListeners.get(eventType).add(weixinEventListener);
  }
}
