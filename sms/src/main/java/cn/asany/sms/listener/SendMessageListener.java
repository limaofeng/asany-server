package cn.asany.sms.listener;

import cn.asany.base.sms.SendFailedException;
import cn.asany.base.sms.ShortMessageServiceProvider;
import cn.asany.sms.domain.ShortMessage;
import cn.asany.base.sms.MessageStatus;
import cn.asany.sms.event.SendMessageEvent;
import cn.asany.sms.provider.ShortMessageServiceProviderFactory;
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

/**
 * 发送短信监听器
 *
 * @author limaofeng
 */
@Slf4j
@Component
public class SendMessageListener implements ApplicationListener<SendMessageEvent> {

  private MessageService messageService;
  private ShortMessageServiceProviderFactory providerFactory;

  @Async
  @Override
  @Transactional(rollbackFor = Exception.class)
  public void onApplicationEvent(SendMessageEvent event) {
    Long id = event.getMessage().getId();
    ShortMessage shortMessage = messageService.get(id);
    if (shortMessage.getStatus() != MessageStatus.unsent) {
      return;
    }
    //noinspection unchecked
    Map<String, String> params = JSON.deserialize(shortMessage.getContent(), HashMap.class);
    ShortMessageServiceProvider provider = providerFactory.getProvider(shortMessage.getProvider());
    try {
      String result =
        provider.send(
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
  public void setProviderFactory(ShortMessageServiceProviderFactory providerFactory) {
    this.providerFactory = providerFactory;
  }

  @Autowired
  public void setMessageService(MessageService messageService) {
    this.messageService = messageService;
  }
}
