package cn.asany.message.define.service;

import cn.asany.message.define.dao.MessageTypeDao;
import cn.asany.message.define.domain.MessageType;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MessageTypeService {

  private final MessageTypeDao messageTypeDao;

  public MessageTypeService(MessageTypeDao messageTypeDao) {
    this.messageTypeDao = messageTypeDao;
  }

  public List<MessageType> findAll() {
    return this.messageTypeDao.findAll();
  }

  public MessageType save(MessageType messageType) {
    return this.messageTypeDao.save(messageType);
  }

  public Optional<MessageType> findById(String id) {
    return this.messageTypeDao.findById(id);
  }
}
