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

    MessageContent content = new MessageContent();

    content.set(MessageContent.SMS_CODE, "test");
    content.set(MessageContent.SMS_SIGN, "测试");

    template.setContent(content);

    MessageChannel<Message> channel = (MessageChannel<Message>) messageChannelResolver.resolve("1");

    channel.send(
        SmsMessage.builder()
            .signName(template.getContent().get(MessageContent.SMS_SIGN))
            .templateCode(template.getContent().get(MessageContent.SMS_CODE))
            .templateParams(new HashMap<>())
            .phones(new String[] {"15921884771"})
            .build());
  }

  @Test
  void testEMailMessage() throws MessageException {
    MessageTemplate template = new MessageTemplate();
    template.setType(TemplateType.EMAIL);
    template.setName("测试短信");

    MessageContent content = new MessageContent();
    content.set(MessageContent.SMS_CODE, "test");
    content.set(MessageContent.SMS_SIGN, "测试");

    template.setContent(content);

    MessageChannel<Message> channel = (MessageChannel<Message>) messageChannelResolver.resolve("1");

    channel.send(
        SmsMessage.builder()
            .signName(template.getContent().get(MessageContent.SMS_SIGN))
            .templateCode(template.getContent().get(MessageContent.SMS_CODE))
            .templateParams(new HashMap<>())
            .phones(new String[] {"15921884771"})
            .build());
  }
}
