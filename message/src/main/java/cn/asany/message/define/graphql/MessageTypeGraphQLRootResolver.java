package cn.asany.message.define.graphql;

import cn.asany.message.define.domain.MessageType;
import cn.asany.message.define.graphql.input.MessageTypeCreateInput;
import cn.asany.message.define.graphql.input.MessageTypeUpdateInput;
import cn.asany.message.define.graphql.mapper.MessageTypeConverter;
import cn.asany.message.define.service.MessageTypeService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class MessageTypeGraphQLRootResolver
    implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final MessageTypeService messageTypeService;

  private final MessageTypeConverter messageTypeConverter;

  public MessageTypeGraphQLRootResolver(
      MessageTypeService messageTypeService, MessageTypeConverter messageTypeConverter) {
    this.messageTypeService = messageTypeService;
    this.messageTypeConverter = messageTypeConverter;
  }

  public Optional<MessageType> messageType(String id) {
    return this.messageTypeService.findById(id);
  }

  public List<MessageType> messageTypes() {
    return this.messageTypeService.findAll();
  }

  public MessageType createMessageType(MessageTypeCreateInput input) {
    return this.messageTypeService.save(messageTypeConverter.toMessageType(input));
  }

  public MessageType updateMessageType(Long id, MessageTypeUpdateInput input) {
    return null;
  }

  public Boolean deleteMessageType(Long id) {
    return null;
  }
}
