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
package cn.asany.crm.support.dao.listener;

import cn.asany.crm.support.domain.Ticket;
import cn.asany.crm.support.event.TicketCreatedEvent;
import net.asany.jfantasy.framework.dao.hibernate.listener.AbstractChangedListener;
import org.hibernate.event.spi.EventType;
import org.hibernate.event.spi.PostInsertEvent;
import org.springframework.stereotype.Component;

@Component("dao.ticketListener")
public class TicketListener extends AbstractChangedListener<Ticket> {

  public TicketListener() {
    super(EventType.POST_COMMIT_INSERT);
  }

  @Override
  protected void onPostInsert(Ticket entity, PostInsertEvent event) {
    this.applicationContext.publishEvent(new TicketCreatedEvent(entity));
  }
}
