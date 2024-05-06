package cn.asany.message.define.domain;

import cn.asany.message.TestApplication;
import cn.asany.message.api.*;
import cn.asany.message.define.domain.enums.TemplateType;
import cn.asany.message.define.domain.toys.MessageContent;
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

  @Autowired private MessageChannelResolver messageChannelResolver;

  @Test
  void testSmsMessage() throws MessageException {
    MessageTemplate template = new MessageTemplate();
    template.setType(TemplateType.SMS);
    template.setName("测试短信");
    template.setSign("测试");
    template.setCode("test");
    template.setContent(MessageContent.of("测试短信内容"));

    MessageChannel channel = messageChannelResolver.resolve("1");

    channel.send(
        SmsMessage.builder()
            .signName(template.getSign())
            .templateCode(template.getCode())
            .templateParams(new HashMap<>())
            .phones(new String[] {"15921884771"})
            .build());
  }

  @Test
  void testEMailMessage() throws MessageException {
    MessageTemplate template = new MessageTemplate();
    template.setType(TemplateType.EMAIL);
    template.setName("测试短信");
    template.setSign("测试");
    template.setCode("test");
    template.setContent(MessageContent.of("测试短信内容"));

    MessageChannel channel = messageChannelResolver.resolve("1");

    channel.send(
      SmsMessage.builder()
        .signName(template.getSign())
        .templateCode(template.getCode())
        .templateParams(new HashMap<>())
        .phones(new String[] {"15921884771"})
        .build());
  }

}
