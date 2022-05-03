package cn.asany.security.core.graphql;

import cn.asany.security.core.service.UserService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.springframework.stereotype.Component;

@Component
public class UserGraphQLQueryAndMutationResolver
    implements GraphQLMutationResolver, GraphQLQueryResolver {

  private final UserService userService;

  public UserGraphQLQueryAndMutationResolver(UserService userService) {
    this.userService = userService;
  }

  public Boolean changePassword(String oldPassword, String newPassword) {
    LoginUser loginUser = SpringSecurityUtils.getCurrentUser();
    userService.changePassword(loginUser.getUid(), oldPassword, newPassword);
    return Boolean.TRUE;
  }
}
