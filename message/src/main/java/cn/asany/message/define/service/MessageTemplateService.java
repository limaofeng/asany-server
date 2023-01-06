package cn.asany.message.define.service;

import cn.asany.message.define.dao.MessageTemplateDao;
import cn.asany.message.define.domain.MessageTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageTemplateService {

  private final MessageTemplateDao messageTemplateDao;

  public MessageTemplateService(MessageTemplateDao messageTemplateDao) {
    this.messageTemplateDao = messageTemplateDao;
  }

  public MessageTemplate save(MessageTemplate template) {
    return this.messageTemplateDao.save(template);
  }
}
