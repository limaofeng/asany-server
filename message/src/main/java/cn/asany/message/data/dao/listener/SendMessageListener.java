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
package cn.asany.message.data.dao.listener;

import cn.asany.message.data.domain.Message;
import cn.asany.message.data.event.MessageCreateEvent;
import net.asany.jfantasy.framework.dao.hibernate.listener.AbstractChangedListener;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostInsertEvent;
import org.hibernate.event.spi.PostUpdateEvent;
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
