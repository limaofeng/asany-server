package cn.asany.message.data.graphql;

import cn.asany.message.data.domain.Message;
import cn.asany.message.data.graphql.input.MessageCreateInput;
import cn.asany.message.data.graphql.mapper.MessageMapper;
import cn.asany.message.data.service.DefaultMessageService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Message GraphQL Root Resolver
 *
 * @author limaofeng
 */
@Component
public class MessageGraphQLRootResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final DefaultMessageService messageService;
  private final MessageMapper messageMapper;

  public MessageGraphQLRootResolver(
      DefaultMessageService messageService, MessageMapper messageMapper) {
    this.messageService = messageService;
    this.messageMapper = messageMapper;
  }

  public Message createMessage(MessageCreateInput input) {
    Map<String, Object> variables = messageMapper.toMessageVariableValues(input.getVariables());
    String id =
        this.messageService.send(
            input.getType(), variables, input.getRecipients().toArray(new String[0]));
    return messageService
        .findById(Long.valueOf(id))
        .orElseThrow(() -> new RuntimeException("消息发送失败"));
  }
}
