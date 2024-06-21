/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.storage.data.listener;

import cn.asany.storage.core.StorageResolver;
import cn.asany.storage.data.domain.StorageConfig;
import net.asany.jfantasy.framework.dao.hibernate.listener.AbstractChangedListener;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostDeleteEvent;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostUpdateEvent;
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
