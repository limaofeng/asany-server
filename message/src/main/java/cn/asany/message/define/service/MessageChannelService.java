package cn.asany.message.define.service;

import cn.asany.message.define.dao.MessageChannelDefinitionDao;
import cn.asany.message.define.domain.MessageChannelDefinition;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 消息定义服务
 *
 * @author limaofeng
 */
@Service
public class MessageChannelService {

  private final MessageChannelDefinitionDao messageChannelDefinitionDao;

  public MessageChannelService(MessageChannelDefinitionDao messageChannelDefinitionDao) {
    this.messageChannelDefinitionDao = messageChannelDefinitionDao;
  }

  public void save(MessageChannelDefinition definition) {
    this.messageChannelDefinitionDao.save(definition);
  }

  @Transactional(readOnly = true)
  public Optional<MessageChannelDefinition> findById(Long id) {
    return this.messageChannelDefinitionDao.findById(id);
  }
}
