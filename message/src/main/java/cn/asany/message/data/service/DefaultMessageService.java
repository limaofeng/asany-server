package cn.asany.message.data.service;

import cn.asany.message.api.MessageService;
import cn.asany.message.data.dao.MessageDao;
import cn.asany.message.data.domain.Message;
import cn.asany.message.data.domain.MessageRecipient;
import cn.asany.message.data.domain.enums.MessageStatus;
import cn.asany.message.define.dao.MessageTypeDao;
import cn.asany.message.define.domain.MessageDefinition;
import cn.asany.message.define.domain.MessageTemplate;
import cn.asany.message.define.domain.MessageType;
import cn.asany.message.define.domain.toys.VariableDefinition;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 消息服务
 *
 * @author limaofeng
 */
@Service
public class DefaultMessageService implements MessageService {

  private final MessageDao messageDao;

  private final MessageTypeDao messageTypeDao;

  public DefaultMessageService(MessageDao messageDao, MessageTypeDao messageTypeDao) {
    this.messageDao = messageDao;
    this.messageTypeDao = messageTypeDao;
  }

  public Message save(Message message) {
    MessageType messageType = this.messageTypeDao.getReferenceById(message.getType().getId());

    MessageDefinition messageDefinition = messageType.getDefinition();
    MessageTemplate messageTemplate = messageDefinition.getTemplate();

    List<VariableDefinition> messageVariableDefinitions = messageDefinition.getVariables();

    Map<String, String> data = messageDefinition.getMappingVariables();

    List<VariableDefinition> templateVariableDefinitions = messageTemplate.getVariables();

    return message; // this.messageDao.save(message);
  }

  @Override
  public String send(String type, String content, String... receivers) {
    return "";
  }

  @Override
  public String send(String type, String title, String content, String... receivers) {
    return "";
  }

  @Override
  @Transactional(rollbackFor = RuntimeException.class)
  public String send(String type, Map<String, Object> variables, String... receivers) {
    MessageType messageType = this.messageTypeDao.getReferenceById(type);

    MessageDefinition messageDefinition = messageType.getDefinition();
    MessageTemplate messageTemplate = messageDefinition.getTemplate();

    messageDefinition.validate(variables);
    messageTemplate.validate(messageDefinition.toTemplateData(variables));

    Message message =
        Message.builder()
            .type(messageType)
            .status(MessageStatus.UNSENT)
            .content("NO_CONTENT")
            .recipients(
                Arrays.stream(receivers)
                    .map(
                        r -> {
                          return MessageRecipient.builder().build();
                        })
                    .collect(Collectors.toList()))
            .variables(variables)
            .build();

    return String.valueOf(message.getId());
  }
}
