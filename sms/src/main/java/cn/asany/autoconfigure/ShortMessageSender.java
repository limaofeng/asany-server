package cn.asany.autoconfigure;

import cn.asany.base.sms.SendFailedException;
import cn.asany.base.sms.ShortMessageServiceProvider;
import cn.asany.message.api.MessageException;
import cn.asany.message.api.MessageSender;
import cn.asany.message.api.SimpleMessage;

/**
 * 短信发送器
 *
 * @author limaofeng
 */
public class ShortMessageSender implements MessageSender {

  private final ShortMessageServiceProvider provider;

  public ShortMessageSender(ShortMessageServiceProvider provider) {
    this.provider = provider;
  }

  @Override
  public void send(SimpleMessage simpleMessage) throws MessageException {
    try {
      provider.send(
          simpleMessage.getTemplateCode(),
          simpleMessage.getTemplateParams(),
          simpleMessage.getSignName(),
          simpleMessage.getTo());
    } catch (SendFailedException e) {
      throw new RuntimeException(e);
    }
  }
}
