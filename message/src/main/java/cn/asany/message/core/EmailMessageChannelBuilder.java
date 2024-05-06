package cn.asany.message.core;

import cn.asany.message.api.MSChannelConfig;
import cn.asany.message.api.MessageChannelBuilder;

public class EmailMessageChannelBuilder
  implements MessageChannelBuilder<EmailMessageChannel, MSChannelConfig> {

  @Override
  public boolean supports(Class<MSChannelConfig> clazz) {
    return MSChannelConfig.class.isAssignableFrom(clazz);
  }

  @Override
  public EmailMessageChannel build(MSChannelConfig clazz) {
    return null;
  }
}
