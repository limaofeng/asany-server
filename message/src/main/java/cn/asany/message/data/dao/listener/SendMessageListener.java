package cn.asany.message.data.dao.listener;

import cn.asany.message.data.domain.Message;
import cn.asany.message.data.event.MessageCreateEvent;
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
@Component("dao.SendMessageListener")
public class SendMessageListener extends AbstractChangedListener<Message> {

  public SendMessageListener() {
    super(EventType.POST_COMMIT_INSERT, EventType.POST_COMMIT_UPDATE);
  }

  @Override
  protected void onPostInsert(Message entity, PostInsertEvent event) {
    this.applicationContext.publishEvent(new MessageCreateEvent(entity));
  }

  @Override
  protected void onPostUpdate(Message entity, PostUpdateEvent event) {
    //    this.applicationContext.publishEvent(new SendMessageEvent(entity));
  }
}
