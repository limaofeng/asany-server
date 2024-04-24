package cn.asany.message.data.graphql;

import cn.asany.message.data.domain.Message;
import cn.asany.message.data.graphql.input.MessageCreateInput;
import cn.asany.message.data.graphql.input.MessageWhereInput;
import cn.asany.message.data.graphql.mapper.MessageMapper;
import cn.asany.message.data.graphql.type.MessageConnection;
import cn.asany.message.data.service.DefaultMessageService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.Map;
import net.asany.jfantasy.graphql.util.Kit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
      DefaultMessageService messageService,
      @Autowired(required = false) MessageMapper messageMapper) {
    this.messageService = messageService;
    this.messageMapper = messageMapper;
  }

  public MessageConnection messages(
      MessageWhereInput where, int first, int page, int pageSize, Sort orderBy) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    return Kit.connection(
        messageService.findPage(pageable, where.toFilter()), MessageConnection.class);
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
