package cn.asany.message.define.service;

import cn.asany.message.define.dao.MessageTemplateDao;
import cn.asany.message.define.domain.MessageTemplate;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 消息模板服务
 *
 * @author limaofeng
 */
@Service
public class MessageTemplateService {

  private final MessageTemplateDao messageTemplateDao;

  public MessageTemplateService(MessageTemplateDao messageTemplateDao) {
    this.messageTemplateDao = messageTemplateDao;
  }

  public MessageTemplate save(MessageTemplate template) {
    return this.messageTemplateDao.save(template);
  }

  @Transactional(readOnly = true)
  public Optional<MessageTemplate> findById(Long id) {
    return this.messageTemplateDao.findById(id);
  }
}
