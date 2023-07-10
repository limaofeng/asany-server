package cn.asany.message.data.service;

import cn.asany.message.TestApplication;
import cn.asany.message.api.MessageService;
import cn.asany.message.data.domain.Message;
import cn.asany.message.data.event.MessageCreateEvent;
import cn.asany.message.data.util.MessageUtils;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class DefaultMessageServiceTest {

  @Autowired private MessageService messageService;
  @Autowired private ApplicationContext applicationContext;

  @Test
  void send() {
    Map<String, Object> params = new HashMap<>();
    messageService.send("task.assignee", params, MessageUtils.formatRecipientFromUser(1L));
  }

  @Test
  void trySend() {
    DefaultMessageService defaultMessageService = (DefaultMessageService) messageService;
    Message message =
        defaultMessageService.findById(7L).orElseThrow(() -> new RuntimeException("消息不存在"));
    applicationContext.publishEvent(new MessageCreateEvent(message));
  }
}
