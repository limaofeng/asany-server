package cn.asany.message.data.dao.listener;

import cn.asany.message.data.domain.UserMessage;
import cn.asany.message.data.event.UserMessageCreateEvent;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostUpdateEvent;
import org.jfantasy.framework.dao.hibernate.listener.AbstractChangedListener;
import org.springframework.stereotype.Component;

/**
 * 用户消息监听器
 *
 * @author limaofeng
 */
@Component("dao.UserMessageListener")
public class UserMessageListener extends AbstractChangedListener<UserMessage> {

  public UserMessageListener() {
    super(EventType.POST_COMMIT_INSERT, EventType.POST_COMMIT_UPDATE);
  }

  @Override
  protected void onPostInsert(UserMessage entity, PostInsertEvent event) {
    this.applicationContext.publishEvent(new UserMessageCreateEvent(entity));
  }

  @Override
  protected void onPostUpdate(UserMessage entity, PostUpdateEvent event) {
    //    this.applicationContext.publishEvent(new SendMessageEvent(entity));
  }
}
