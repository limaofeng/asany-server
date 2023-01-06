package cn.asany.message.define.graphql.resolver;

import cn.asany.message.define.domain.MessageDefinitionReminder;
import cn.asany.message.define.graphql.type.MessageMappingVariable;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MessageDefinitionReminderGraphQLResolver
    implements GraphQLResolver<MessageDefinitionReminder> {

  public List<MessageMappingVariable> mappingVariables(MessageDefinitionReminder definition) {
    List<MessageMappingVariable> mappingVariables = new ArrayList<>();
    for (Map.Entry<String, String> entry : definition.getMappingVariables().entrySet()) {
      mappingVariables.add(
          MessageMappingVariable.builder().key(entry.getKey()).value(entry.getValue()).build());
    }
    return mappingVariables;
  }
}
