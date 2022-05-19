package cn.asany.weixin.listener;

import cn.asany.weixin.framework.event.SubscribeEventListener;
import cn.asany.weixin.framework.exception.WeixinException;
import cn.asany.weixin.framework.factory.WeixinSessionUtils;
import cn.asany.weixin.framework.message.EventMessage;
import cn.asany.weixin.framework.message.content.Event;
import cn.asany.weixin.framework.session.WeixinSession;
import cn.asany.weixin.service.FansService;
import java.util.concurrent.Executor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jfantasy.framework.spring.SpringBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

public class SubscribeListener implements SubscribeEventListener {

  private static final Log LOG = LogFactory.getLog(SubscribeListener.class);

  @Autowired private FansService fansService;

  @Override
  public void onSubscribe(final WeixinSession session, Event event, final EventMessage<?> message) {
    Executor executor = SpringBeanUtils.getBeanByType(Executor.class);
    assert executor != null;
    executor.execute(
        () -> {
          try {
            WeixinSessionUtils.saveSession(session);
            fansService.checkCreateMember(
                session.getWeixinApp().getId(), message.getFromUserName());
          } catch (WeixinException e) {
            LOG.error(e.getMessage(), e);
          } finally {
            WeixinSessionUtils.closeSession();
          }
        });
  }
}
