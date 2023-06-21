package cn.asany.message.define.domain;

import static org.junit.jupiter.api.Assertions.*;

import cn.asany.message.TestApplication;
import cn.asany.message.api.MessageException;
import cn.asany.message.api.MessageSender;
import cn.asany.message.api.MessageSenderResolver;
import cn.asany.message.api.SimpleMessage;
import cn.asany.message.define.domain.enums.TemplateType;
import java.util.HashMap;
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
class MessageTemplateTest {

  @Autowired private MessageSenderResolver messageSenderResolver;

  @Test
  void testSmsMessage() throws MessageException {
    MessageTemplate template = new MessageTemplate();
    template.setType(TemplateType.SMS);
    template.setName("测试短信");
    template.setSign("测试");
    template.setCode("test");
    template.setContent("测试短信内容");

    MessageSender sender = messageSenderResolver.resolve("1");
    sender.send(
        SimpleMessage.builder()
            .signName(template.getSign())
            .templateCode(template.getCode())
            .templateParams(new HashMap<>())
            .from("test")
            .to(new String[] {"15921884771"})
            .build());
  }
}
