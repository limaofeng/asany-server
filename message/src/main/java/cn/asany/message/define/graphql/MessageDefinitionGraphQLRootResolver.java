package cn.asany.message.define.graphql;

import cn.asany.message.define.domain.MessageDefinition;
import cn.asany.message.define.graphql.input.MessageDefinitionCreateInput;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

@Component
public class MessageDefinitionGraphQLRootResolver
    implements GraphQLQueryResolver, GraphQLMutationResolver {

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
