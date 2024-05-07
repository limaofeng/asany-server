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
package cn.asany.crm.support.listener;

import cn.asany.crm.support.domain.Ticket;
import cn.asany.crm.support.event.TicketCreatedEvent;
import cn.asany.message.api.MessageService;
import cn.asany.message.api.util.MessageUtils;
import java.util.Map;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class TicketCreatedEventListener implements ApplicationListener<TicketCreatedEvent> {

  private final MessageService messageService;

  public TicketCreatedEventListener(MessageService messageService) {
    this.messageService = messageService;
  }

  @Override
  public void onApplicationEvent(TicketCreatedEvent event) {
    Ticket ticket = (Ticket) event.getSource();
    Map<String, Object> params = ObjectUtil.toMap(ticket);
    messageService.send("ticket.new", params, MessageUtils.formatRecipientFromUser(1L));
  }
}
