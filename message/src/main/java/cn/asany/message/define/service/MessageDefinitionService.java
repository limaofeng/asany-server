package cn.asany.message.define.service;

import cn.asany.message.define.dao.MessageDefinitionDao;
import cn.asany.message.define.domain.MessageDefinition;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class MessageDefinitionService {

  private final MessageDefinitionDao messageDefinitionDao;

  public MessageDefinitionService(MessageDefinitionDao messageDefinitionDao) {
    this.messageDefinitionDao = messageDefinitionDao;
  }

  public void save(MessageDefinition definition) {
    this.messageDefinitionDao.save(definition);
  }

  public Optional<MessageDefinition> findById(Long id) {
    return this.messageDefinitionDao.findById(id);
  }
}
