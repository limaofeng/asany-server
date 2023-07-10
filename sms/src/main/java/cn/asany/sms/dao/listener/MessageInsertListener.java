package cn.asany.sms.dao.listener;

import cn.asany.sms.domain.ShortMessage;
import cn.asany.sms.event.SendMessageEvent;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostUpdateEvent;
import org.jfantasy.framework.dao.hibernate.listener.AbstractChangedListener;
import org.springframework.stereotype.Component;

/**
 * 发送短信监听器
 *
 * @author limaofeng
 */
@Component("ShortMessageInsertListener")
public class MessageInsertListener extends AbstractChangedListener<ShortMessage> {

  public MessageInsertListener() {
    super(EventType.POST_COMMIT_INSERT, EventType.POST_COMMIT_UPDATE);
  }

  @Override
  protected void onPostInsert(ShortMessage entity, PostInsertEvent event) {
    this.applicationContext.publishEvent(new SendMessageEvent(entity));
  }

  @Override
  protected void onPostUpdate(ShortMessage entity, PostUpdateEvent event) {
    this.applicationContext.publishEvent(new SendMessageEvent(entity));
  }
}
