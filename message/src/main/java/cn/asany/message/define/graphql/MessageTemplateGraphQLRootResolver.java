package cn.asany.message.define.graphql;

import cn.asany.message.define.domain.MessageTemplate;
import cn.asany.message.define.graphql.input.MessageTemplateCreateInput;
import cn.asany.message.define.graphql.mapper.MessageTemplateConverter;
import cn.asany.message.define.service.MessageTemplateService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

/**
 * 消息模板 GraphQL 根解析器
 *
 * @author limaofeng
 */
@Component
public class MessageTemplateGraphQLRootResolver
    implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final MessageTemplateService messageTemplateService;
  private final MessageTemplateConverter messageTemplateConverter;

  public MessageTemplateGraphQLRootResolver(
      MessageTemplateService messageTemplateService,
      MessageTemplateConverter messageTemplateConverter) {
    this.messageTemplateService = messageTemplateService;
    this.messageTemplateConverter = messageTemplateConverter;
  }

  public MessageTemplate createMessageTemplate(MessageTemplateCreateInput input) {
    return this.messageTemplateService.save(this.messageTemplateConverter.toMessageTemplate(input));
  }
}
