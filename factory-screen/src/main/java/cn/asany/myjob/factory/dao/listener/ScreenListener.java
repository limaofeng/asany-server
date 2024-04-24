package cn.asany.myjob.factory.dao.listener;

import cn.asany.myjob.factory.domain.Screen;
import cn.asany.myjob.factory.listener.event.ScreenChangeEvent;
import cn.asany.myjob.factory.listener.event.ScreenCreateEvent;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostUpdateEvent;
import net.asany.jfantasy.framework.dao.hibernate.listener.AbstractChangedListener;
import org.springframework.stereotype.Component;

@Component("dao.ScreenListener")
public class ScreenListener extends AbstractChangedListener<Screen> {

  public ScreenListener() {
    super(EventType.POST_COMMIT_INSERT, EventType.POST_COMMIT_UPDATE);
  }

  @Override
  protected void onPostInsert(Screen entity, PostInsertEvent event) {
    this.applicationContext.publishEvent(new ScreenCreateEvent(entity));
  }

  @Override
  protected void onPostUpdate(Screen entity, PostUpdateEvent event) {
    this.applicationContext.publishEvent(new ScreenChangeEvent(entity));
  }
}
