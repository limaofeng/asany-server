package cn.asany.sms.listener;

import cn.asany.base.sms.SendFailedException;
import cn.asany.base.sms.ShortMessageSendService;
import cn.asany.sms.domain.ShortMessage;
import cn.asany.sms.domain.enums.MessageStatus;
import cn.asany.sms.event.SendMessageEvent;
import cn.asany.sms.service.MessageService;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.jackson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class SendMessageListener implements ApplicationListener<SendMessageEvent> {

  private MessageService messageService;
  private ShortMessageSendService smsService;

  @Override
  @Async
  @Transactional
  public void onApplicationEvent(SendMessageEvent event) {
    Long id = event.getMessage().getId();
    ShortMessage shortMessage = messageService.get(id);
    if (shortMessage.getStatus() != MessageStatus.unsent) {
      return;
    }
    Map<String, String> params = JSON.deserialize(shortMessage.getContent(), HashMap.class);
    try {
      String result =
          smsService.send(
              shortMessage.getTemplate().getCode(),
              params,
              shortMessage.getSign(),
              shortMessage.getPhones().toArray(new String[0]));
      shortMessage.setStatus(MessageStatus.success);
      shortMessage.setNotes(result);
    } catch (SendFailedException e) {
      log.error(e.getMessage(), e);
      shortMessage.setStatus(MessageStatus.failure);
      shortMessage.setNotes(e.getMessage());
    }
    this.messageService.update(shortMessage);
  }

  @Autowired
  public void setSmsService(ShortMessageSendService smsService) {
    this.smsService = smsService;
  }

  @Autowired
  public void setMessageService(MessageService messageService) {
    this.messageService = messageService;
  }
}
