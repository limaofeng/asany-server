package cn.asany.storage.data.listener;

import cn.asany.storage.core.StorageResolver;
import cn.asany.storage.data.domain.StorageConfig;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostUpdateEvent;
import org.jfantasy.framework.dao.hibernate.listener.AbstractChangedListener;
import org.springframework.stereotype.Component;

@Component
public class StorageEventListener extends AbstractChangedListener<StorageConfig> {

  private final StorageResolver storageResolver;

  public StorageEventListener(StorageResolver storageResolver) {
    super(EventType.POST_INSERT, EventType.POST_UPDATE, EventType.POST_DELETE);
    this.storageResolver = storageResolver;
  }

  @Override
  public void onPostDelete(StorageConfig config, PostDeleteEvent event) {}

  @Override
  public void onPostUpdate(StorageConfig config, PostUpdateEvent event) {
    if (modify(event, "details")) {
      storageResolver.resolve(config.getProperties());
    }
  }

  @Override
  public void onPostInsert(StorageConfig config, PostInsertEvent event) {
    storageResolver.resolve(config.getProperties());
  }
}
