package cn.asany.message.data.service;

import cn.asany.message.data.dao.MessageDao;
import cn.asany.message.data.domain.Message;
import cn.asany.message.define.dao.MessageTypeDao;
import cn.asany.message.define.domain.MessageDefinition;
import cn.asany.message.define.domain.MessageTemplate;
import cn.asany.message.define.domain.MessageType;
import cn.asany.message.define.domain.toys.VariableDefinition;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * 消息服务
 *
 * @author limaofeng
 */
@Service
public class MessageService {

  private final MessageDao messageDao;

  private final MessageTypeDao messageTypeDao;

  public MessageService(MessageDao messageDao, MessageTypeDao messageTypeDao) {
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
}
