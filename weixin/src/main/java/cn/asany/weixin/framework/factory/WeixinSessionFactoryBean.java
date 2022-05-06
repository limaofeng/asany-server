package cn.asany.weixin.framework.factory;

import cn.asany.weixin.framework.account.WeixinAppService;
import cn.asany.weixin.framework.core.MpCoreHelper;
import cn.asany.weixin.framework.core.WeixinCoreHelper;
import cn.asany.weixin.framework.event.WeixinEventListener;
import cn.asany.weixin.framework.handler.DefaultEventHandler;
import cn.asany.weixin.framework.handler.NotReplyTextHandler;
import cn.asany.weixin.framework.handler.WeixinHandler;
import cn.asany.weixin.framework.intercept.LogInterceptor;
import cn.asany.weixin.framework.intercept.WeixinMessageInterceptor;
import cn.asany.weixin.framework.message.EventMessage;
import cn.asany.weixin.framework.session.DefaultWeixinSession;
import cn.asany.weixin.framework.session.WeixinApp;
import cn.asany.weixin.framework.session.WeixinSession;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.spring.SpringBeanUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.ApplicationContext;

public class WeixinSessionFactoryBean implements FactoryBean<WeixinSessionFactory> {

  private static final Log LOG = LogFactory.getLog(WeixinSessionFactoryBean.class);

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

    //    if (this.weixinAppService == null) {
    //      this.weixinAppService = SpringBeanUtils.getBeanByType(AppService.class);
    //    }
    factory.setWeixinAppService(this.weixinAppService);

    if (this.weixinCoreHelper != null) {
      factory.setWeixinCoreHelper(this.weixinCoreHelper);
    } else {
      factory.setWeixinCoreHelper(SpringBeanUtils.getBeanByType(MpCoreHelper.class));
    }

    factory.setMessageHandler(this.messageHandler);
    factory.setEventHandler(this.eventHandler);
    factory.setSessionClass(sessionClass);
    factory.setWeixinMessageInterceptors(weixinMessageInterceptors);
    factory.setEventListeners(this.eventListeners);

    this.weixinSessionFactory = factory;

    for (WeixinApp weixinApp : weixinSessionFactory.getWeixinAppService().getAll()) {
      weixinSessionFactory.getWeixinCoreHelper().register(weixinApp);
    }

    LOG.error("\n初始化 WeiXinSessionFactory 耗时:" + (System.currentTimeMillis() - start) + "ms");
  }

  @Override
  public WeixinSessionFactory getObject() throws Exception {
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
