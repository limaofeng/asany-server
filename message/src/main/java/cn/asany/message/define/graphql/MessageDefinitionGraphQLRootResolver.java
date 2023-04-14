package cn.asany.message.define.graphql;

import cn.asany.message.define.domain.MessageDefinition;
import cn.asany.message.define.graphql.input.MessageDefinitionCreateInput;
import cn.asany.message.define.service.MessageDefinitionService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class MessageDefinitionGraphQLRootResolver
    implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final MessageDefinitionService messageDefinitionService;

  public MessageDefinitionGraphQLRootResolver(MessageDefinitionService messageDefinitionService) {
    this.messageDefinitionService = messageDefinitionService;
  }

  public Optional<MessageDefinition> messageDefinition(Long id) {
    return this.messageDefinitionService.findById(id);
  }

  public MessageDefinition createMessageDefinition(MessageDefinitionCreateInput input) {
    return null;
  }

  public MessageDefinition updateMessageDefinition(Long id, MessageDefinitionCreateInput input) {
    return null;
  }

  public MessageDefinition deleteMessageDefinition(Long id) {
    return null;
  }
}
