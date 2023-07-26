package cn.asany.message.data.graphql;

import cn.asany.message.data.graphql.input.UserMessageWhereInput;
import cn.asany.message.data.graphql.type.UserMessageConnection;
import cn.asany.message.data.service.UserMessageService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * 用户消息 GraphQL Root Resolver
 *
 * @author limaofeng
 */
@Component
public class UserMessageGraphQLRootResolver
    implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final UserMessageService userMessageService;

  public UserMessageGraphQLRootResolver(UserMessageService userMessageService) {
    this.userMessageService = userMessageService;
  }

  public UserMessageConnection userMessages(
      UserMessageWhereInput where, int first, int page, int pageSize, Sort orderBy) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    return Kit.connection(
        userMessageService.findPage(pageable, where.toFilter()), UserMessageConnection.class);
  }
}
