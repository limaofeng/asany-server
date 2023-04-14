package cn.asany.message.core;

import cn.asany.message.api.*;
import cn.asany.message.define.domain.MessageSenderDefinition;
import cn.asany.message.define.service.MessageSenderService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jfantasy.framework.jackson.JSON;

/**
 * 默认的消息发送者解析器
 *
 * @author limaofeng
 */
public class DefaultMessageSenderResolver implements MessageSenderResolver {

  private final MessageSenderService messageSenderService;
  private final Map<String, MessageSender> messageSenderMap = new HashMap<>();
  private final List<MessageSenderBuilder<?, ?>> builders;

  public DefaultMessageSenderResolver(
      MessageSenderService messageSenderService, List<MessageSenderBuilder<?, ?>> builders) {
    this.builders = builders;
    this.messageSenderService = messageSenderService;
  }

  @Override
  public MessageSender resolve(String id) throws MessageException {
    if (messageSenderMap.containsKey(id)) {
      return messageSenderMap.get(id);
    }
    MessageSenderDefinition definition = messageSenderService.get(Long.valueOf(id));
    return resolve(definition.getId().toString(), definition.getSenderConfig());
  }

  @Override
  public MessageSender resolve(String id, ISenderConfig config) throws MessageException {
    for (MessageSenderBuilder builder : builders) {
      if (builder.supports(config.getClass())) {
        MessageSender sender = builder.build(config);
        if (sender != null) {
          messageSenderMap.put(id, sender);
          return sender;
        }
      }
    }
    throw new MessageException("不能创建 MessageSender,  Config = " + JSON.serialize(config));
  }
}
