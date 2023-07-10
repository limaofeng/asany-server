package cn.asany.message.define.service;

import cn.asany.message.define.dao.MessageDefinitionDao;
import cn.asany.message.define.domain.MessageDefinition;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 消息定义服务
 *
 * @author limaofeng
 */
@Service
public class MessageDefinitionService {

  private final MessageDefinitionDao messageDefinitionDao;

  public MessageDefinitionService(MessageDefinitionDao messageDefinitionDao) {
    this.messageDefinitionDao = messageDefinitionDao;
  }

  public void save(MessageDefinition definition) {
    this.messageDefinitionDao.save(definition);
  }

  @Transactional(readOnly = true)
  public Optional<MessageDefinition> findById(Long id) {
    return this.messageDefinitionDao.findById(id);
  }
}
