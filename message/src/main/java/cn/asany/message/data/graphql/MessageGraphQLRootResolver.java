package cn.asany.message.data.graphql;

import cn.asany.message.data.domain.Message;
import cn.asany.message.data.graphql.input.MessageCreateInput;
import cn.asany.message.data.graphql.mapper.MessageMapper;
import cn.asany.message.data.service.MessageService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

/**
 * Message GraphQL Root Resolver
 *
 * @author limaofeng
 */
@Component
public class MessageGraphQLRootResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final MessageService messageService;
  private final MessageMapper messageMapper;

  public MessageGraphQLRootResolver(MessageService messageService, MessageMapper messageMapper) {
    this.messageService = messageService;
    this.messageMapper = messageMapper;
  }

  public Message createMessage(MessageCreateInput input) {
    return this.messageService.save(messageMapper.toMessage(input));
  }
}
