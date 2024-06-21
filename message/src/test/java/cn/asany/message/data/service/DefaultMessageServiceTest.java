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
package cn.asany.message.data.service;

import cn.asany.message.TestApplication;
import cn.asany.message.api.MessageService;
import cn.asany.message.api.util.MessageUtils;
import cn.asany.message.data.domain.Message;
import cn.asany.message.data.event.MessageCreateEvent;
import cn.asany.security.core.domain.User;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.DataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class DefaultMessageServiceTest {

  @Autowired private MessageService messageService;
  @Autowired private ApplicationContext applicationContext;

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
  void send() {
    Map<String, Object> params = new HashMap<>();
    params.put("id", "1111");
    params.put("no", "1231");
    Map<String, Object> store = new HashMap<>();
    store.put("name", "测试门店");
    params.put("store", store);
    String msgId =
        messageService.send("ticket.new", params, MessageUtils.formatRecipientFromUser(1L));
    log.info("消息ID: {}", msgId);
  }

  @Test
  void trySend() {
    DefaultMessageService defaultMessageService = (DefaultMessageService) messageService;

    Message message =
        defaultMessageService.findById(7L).orElseThrow(() -> new RuntimeException("消息不存在"));
    applicationContext.publishEvent(new MessageCreateEvent(message));
  }
}
