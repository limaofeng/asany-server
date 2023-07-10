package cn.asany.message.define.service;

import static org.junit.jupiter.api.Assertions.*;

import cn.asany.message.TestApplication;
import cn.asany.message.define.domain.MessageTemplate;
import java.util.Optional;
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
class MessageTemplateServiceTest {

  @Autowired MessageTemplateService messageTemplateService;

  @Test
  void save() {}

  @Test
  void findById() {
    Optional<MessageTemplate> templateOptional = messageTemplateService.findById(1L);
    log.info("templateOptional:{}", templateOptional);
    assertTrue(templateOptional.isPresent());
  }
}
