package cn.asany.message.define.service;

import cn.asany.message.define.dao.MessageSenderDefinitionDao;
import cn.asany.message.define.domain.MessageSenderDefinition;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 消息发送者定义服务
 *
 * @author limaofeng
 */
@Service
public class MessageSenderService {

  private final MessageSenderDefinitionDao messageSenderDefinitionDao;

  public MessageSenderService(MessageSenderDefinitionDao messageSenderDefinitionDao) {
    this.messageSenderDefinitionDao = messageSenderDefinitionDao;
  }

  @Transactional(readOnly = true)
  public Optional<MessageSenderDefinition> findById(Long id) {
    return messageSenderDefinitionDao.findById(id);
  }
}
