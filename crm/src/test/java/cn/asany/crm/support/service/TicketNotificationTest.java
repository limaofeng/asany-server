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
package cn.asany.crm.support.service;

import cn.asany.crm.TestConfiguration;
import cn.asany.crm.support.domain.Ticket;
import cn.asany.message.api.MessageService;
import cn.asany.message.api.util.MessageUtils;
import cn.asany.security.core.domain.User;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import org.dataloader.DataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = TestConfiguration.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TicketNotificationTest {

  @Autowired private MessageService messageService;
  @Autowired private TicketService ticketService;

  @Autowired
  @Qualifier("user.DataLoader")
  public DataLoader<Long, User> dataLoader;

  @BeforeEach
  public void setUp() {
    // 创建一个单线程的ScheduledExecutorService
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    // 每1秒调度一次 dataLoader.dispatch()
    scheduler.scheduleAtFixedRate(() -> dataLoader.dispatch(), 0, 1, TimeUnit.SECONDS);
  }

  @Test
  public void testTicketNew() {
    Ticket ticket = ticketService.findById(11L).orElseThrow();
    Map<String, Object> params = ObjectUtil.toMap(ticket);
    String msgId =
        messageService.send("ticket.new", params, MessageUtils.formatRecipientFromUser(1L));
    log.info("消息ID: {}", msgId);
  }
}
