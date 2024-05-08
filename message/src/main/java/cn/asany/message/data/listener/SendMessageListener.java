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
package cn.asany.message.data.listener;

import cn.asany.message.api.*;
import cn.asany.message.api.enums.MessageRecipientType;
import cn.asany.message.data.domain.Message;
import cn.asany.message.data.domain.MessageRecipient;
import cn.asany.message.data.domain.enums.MessageStatus;
import cn.asany.message.data.event.MessageCreateEvent;
import cn.asany.message.data.util.MessageUtils;
import cn.asany.message.define.domain.*;
import cn.asany.message.define.domain.enums.TemplateType;
import cn.asany.message.define.domain.toys.MessageContent;
import cn.asany.message.define.service.MessageChannelService;
import cn.asany.message.define.service.MessageDefinitionService;
import cn.asany.message.define.service.MessageTemplateService;
import cn.asany.message.define.service.MessageTypeService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.error.ValidationException;
import net.asany.jfantasy.framework.util.HandlebarsTemplateUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 发送消息监听器
 *
 * @author limaofeng
 */
@Slf4j
@Component("app.SendMessageListener")
public class SendMessageListener implements ApplicationListener<MessageCreateEvent> {

  private final MessageTypeService messageTypeService;
  private final MessageDefinitionService messageDefinitionService;

  private final MessageChannelService messageChannelService;
  private final MessageTemplateService messageTemplateService;

  private final MessageChannelResolver messageChannelResolver;

  private final PlatformTransactionManager transactionManager;

  public SendMessageListener(
      MessageTypeService messageTypeService,
      MessageDefinitionService messageDefinitionService,
      MessageTemplateService messageTemplateService,
      MessageChannelResolver messageChannelResolver,
      MessageChannelService messageChannelService,
      PlatformTransactionManager transactionManager) {
    this.messageTypeService = messageTypeService;
    this.messageDefinitionService = messageDefinitionService;
    this.messageTemplateService = messageTemplateService;
    this.messageChannelResolver = messageChannelResolver;
    this.messageChannelService = messageChannelService;
    this.transactionManager = transactionManager;
  }

  @Override
  public void onApplicationEvent(MessageCreateEvent event) {
    Message message = (Message) event.getSource();

    TransactionTemplate template = new TransactionTemplate(transactionManager);
    template.execute(
        status -> {
          try {

            MessageType messageType =
                this.messageTypeService
                    .findById(message.getType().getId())
                    .orElseThrow(() -> new ValidationException("消息类型不存在"));

            MessageDefinition messageDefinition =
                this.messageDefinitionService
                    .findById(messageType.getDefinition().getId())
                    .orElseThrow(() -> new ValidationException("消息定义不存在"));

            Map<String, Object> variables = message.getVariables();
            Map<String, Object> templateData =
                MessageUtils.toData(variables, messageDefinition.getMappingVariables());

            messageDefinition.validate(variables);

            sendMessage(
                messageDefinition.getTemplate().getId(),
                messageDefinition.getChannel().getId(),
                templateData,
                message);

            // 发送提醒
            for (MessageReminderDefinition messageReminderDefinition :
                messageDefinition.getReminders()) {
              ReminderDefinition reminderDefinition =
                  messageReminderDefinition.getReminderDefinition();

              templateData =
                  MessageUtils.toData(
                      message.getVariables(), messageReminderDefinition.getMappingVariables());

              sendMessage(
                  reminderDefinition.getTemplate().getId(),
                  reminderDefinition.getChannel().getId(),
                  templateData,
                  message);
            }

          } catch (Throwable e) {
            message.setStatus(MessageStatus.FAILURE);
            message.setFailureReason(e.getMessage());
            log.error(e.getMessage(), e);
            throw new ValidationException(e.getMessage());
          }
          return null;
        });
  }

  private void sendMessage(
      Long templateId, Long channelId, Map<String, Object> variables, Message message)
      throws MessageException {

    MessageTemplate messageTemplate =
        this.messageTemplateService
            .findById(templateId)
            .orElseThrow(() -> new ValidationException("消息模板不存在"));

    MessageChannelDefinition messageChannelDefinition =
        this.messageChannelService
            .findById(channelId)
            .orElseThrow(() -> new ValidationException("消息发送者不存在"));

    if (messageChannelDefinition.getType() != messageTemplate.getType()) {
      throw new ValidationException("消息发送器与模版类型不匹配");
    }

    messageTemplate.validate(variables);

    TemplateType messageHandlerType = messageChannelDefinition.getType();
    MessageRecipientType recipientType = messageChannelDefinition.getType().getRecipientType();

    //noinspection rawtypes
    MessageChannel messageChannel = messageChannelResolver.resolve(String.valueOf(channelId));

    // 解析接收者
    List<String> recipients = new ArrayList<>();
    for (MessageRecipient recipient : message.getRecipients()) {
      if (recipientType == MessageRecipientType.EMAIL) {
        recipients.addAll(
            MessageUtils.parseRecipientEmail(recipient.getType(), recipient.getValue()));
      } else if (recipientType == MessageRecipientType.PHONE) {
        recipients.addAll(
            MessageUtils.parseRecipientPhoneNumber(recipient.getType(), recipient.getValue()));
      } else if (recipientType == MessageRecipientType.USER) {
        recipients.addAll(
            MessageUtils.parseRecipientUserId(recipient.getType(), recipient.getValue()));
      } else {
        throw new ValidationException("不支持的消息接收者类型");
      }
    }
    if (recipients.isEmpty()) {
      throw new ValidationException("消息接收者不能为空");
    }

    switch (messageHandlerType) {
      case MS -> sendMsMessage(messageChannel, messageTemplate, message, recipients, variables);
      case SMS -> sendSmsMessage(messageChannel, messageTemplate, recipients, variables);
      case EMAIL -> sendEmailMessage(messageChannel, messageTemplate, recipients, variables);
      default -> throw new ValidationException("不支持的消息发送者类型");
    }
  }

  private void sendEmailMessage(
      MessageChannel<cn.asany.message.api.Message> messageChannel,
      MessageTemplate messageTemplate,
      List<String> recipients,
      Map<String, Object> templateData)
      throws MessageException {
    // 解析消息内容
    MessageContent messageContent = messageTemplate.getContent();

    String title =
        HandlebarsTemplateUtils.processTemplateIntoString(messageContent.getTitle(), templateData);
    String content =
        HandlebarsTemplateUtils.processTemplateIntoString(
            messageContent.getContent(), templateData);

    messageChannel.send(
        EmailMessage.builder()
            .subject(title)
            .text(content)
            .to(recipients.toArray(new String[0]))
            .build());
  }

  private void sendMsMessage(
      MessageChannel<cn.asany.message.api.Message> messageChannel,
      MessageTemplate messageTemplate,
      Message message,
      List<String> recipients,
      Map<String, Object> templateData)
      throws MessageException {
    // 解析消息内容
    MessageContent messageContent = messageTemplate.getContent();
    String title = messageContent.getTitle();
    String content = messageContent.getContent();

    messageChannel.send(
        SimpleMessage.builder()
            .templateParams(templateData)
            .subject(title)
            .text(content)
            .sentDate(message.getCreatedAt())
            .to(recipients.toArray(new String[0]))
            .originalMessage(message)
            .originalMessageType(message.getType())
            .build());
  }

  public void sendSmsMessage(
      MessageChannel<cn.asany.message.api.Message> messageChannel,
      MessageTemplate messageTemplate,
      List<String> recipients,
      Map<String, Object> params)
      throws MessageException {
    // 解析模版参数
    Map<String, String> templateParams = new HashMap<>(params.size());
    params.forEach((key, value) -> templateParams.put(key, String.valueOf(value)));

    MessageContent content = messageTemplate.getContent();

    String code = content.get(MessageContent.SMS_CODE);
    String sign = content.get(MessageContent.SMS_SIGN);

    messageChannel.send(
        SmsMessage.builder()
            .templateCode(code)
            .templateParams(templateParams)
            .signName(sign)
            .phones(recipients.toArray(new String[0]))
            .build());
  }
}
