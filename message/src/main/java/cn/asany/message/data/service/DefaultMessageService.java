package cn.asany.message.data.service;

import cn.asany.message.api.MessageService;
import cn.asany.message.data.dao.MessageDao;
import cn.asany.message.data.domain.Message;
import cn.asany.message.data.domain.enums.MessageStatus;
import cn.asany.message.define.domain.MessageType;
import cn.asany.message.define.domain.toys.MessageContent;
import cn.asany.message.define.service.MessageTypeService;
import java.util.Map;
import java.util.Optional;
import org.hibernate.Hibernate;
import org.jfantasy.framework.error.ValidationException;
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

  private final MessageTypeService messageTypeService;

  public DefaultMessageService(MessageDao messageDao, MessageTypeService messageTypeService) {
    this.messageDao = messageDao;
    this.messageTypeService = messageTypeService;
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

    if (receivers.length == 0) {
      throw new ValidationException("接收人不能为空");
    }

    MessageType messageType =
        this.messageTypeService
            .findById(type)
            .orElseThrow(() -> new ValidationException("消息类型不存在"));

    Message message =
        Message.builder()
            .type(messageType)
            .status(MessageStatus.UNSENT)
            .content(MessageContent.empty())
            .recipients(receivers)
            .variables(variables)
            .build();
    this.messageDao.save(message);
    return String.valueOf(message.getId());
  }

  @Transactional(readOnly = true)
  public Optional<Message> findById(Long id) {
    return this.messageDao
        .findById(id)
        .map(
            msg -> {
              msg.getRecipients().forEach(Hibernate::unproxy);
              return msg;
            });
  }
}
