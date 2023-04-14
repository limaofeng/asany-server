package cn.asany.autoconfigure;

import cn.asany.message.api.MessageSenderBuilder;
import cn.asany.message.api.SMSSenderConfig;

public class ShortMessageSenderBuilder
    implements MessageSenderBuilder<ShortMessageSender, SMSSenderConfig> {
  @Override
  public boolean supports(Class<SMSSenderConfig> clazz) {
    return SMSSenderConfig.class.isAssignableFrom(clazz);
  }

  @Override
  public ShortMessageSender build(SMSSenderConfig clazz) {
    return null;
  }
}
