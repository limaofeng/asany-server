package cn.asany.message.data.service;

import static org.junit.jupiter.api.Assertions.*;

import cn.asany.message.TestApplication;
import cn.asany.message.api.MessageService;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@SpringBootTest(
    classes = TestApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class DefaultMessageServiceTest {

  @Autowired private MessageService messageService;

  @Test
  void send() {
    Map<String, Object> params = new HashMap<>();
    messageService.send("task. assignee", params, "18600000000");
  }
}
