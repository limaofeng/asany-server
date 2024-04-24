package cn.asany.message.core;

import cn.asany.message.api.*;
import cn.asany.message.define.domain.MessageChannelDefinition;
import cn.asany.message.define.service.MessageChannelService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.asany.jfantasy.framework.jackson.JSON;
import org.springframework.stereotype.Component;

/**
 * 默认的消息发送者解析器
 *
 * @author limaofeng
 */
@Component
public class DefaultMessageChannelResolver implements MessageChannelResolver {

  private final MessageChannelService messageChannelService;
  private final Map<String, MessageChannel> messageSenderMap = new HashMap<>();
  private final List<MessageChannelBuilder<?, ?>> builders;

  public DefaultMessageChannelResolver(
      MessageChannelService messageChannelService, List<MessageChannelBuilder<?, ?>> builders) {
    this.builders = builders;
    this.messageChannelService = messageChannelService;
  }

  @Override
  public MessageChannel resolve(String id) throws MessageException {
    if (messageSenderMap.containsKey(id)) {
      return messageSenderMap.get(id);
    }
    MessageChannelDefinition definition =
        messageChannelService
            .findById(Long.valueOf(id))
            .orElseThrow(() -> new MessageException("未找到 MessageSender,  ID = " + id));
    return resolve(definition.getId().toString(), definition.getChannelConfig());
  }

  @Override
  public MessageChannel resolve(String id, IChannelConfig config) throws MessageException {
    //noinspection rawtypes
    for (MessageChannelBuilder builder : builders) {
      //noinspection unchecked
      if (builder.supports(config.getClass())) {
        //noinspection unchecked
        MessageChannel sender = builder.build(config);
        if (sender != null) {
          messageSenderMap.put(id, sender);
          return sender;
        }
      }
    }
    throw new MessageException("不能创建 MessageSender,  Config = " + JSON.serialize(config));
  }
}
