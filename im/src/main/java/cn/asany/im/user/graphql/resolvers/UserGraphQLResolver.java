package cn.asany.im.user.graphql.resolvers;

import cn.asany.im.auth.graphql.type.OnlineStatusDetails;
import cn.asany.im.auth.graphql.type.Platform;
import cn.asany.im.auth.service.AuthService;
import cn.asany.im.error.OpenIMServerAPIException;
import cn.asany.im.user.service.UserService;
import cn.asany.im.utils.OpenIMUtils;
import cn.asany.security.core.domain.User;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

@Component("im.user.UserGraphQLResolver")
public class UserGraphQLResolver implements GraphQLResolver<User> {

  private final AuthService authService;
  private final UserService userService;

  public UserGraphQLResolver(AuthService authService, UserService userService) {
    this.authService = authService;
    this.userService = userService;
  }

  public String imToken(User user, Platform platform) throws OpenIMServerAPIException {
    return this.authService.token(platform, String.valueOf(user.getId()));
  }

  public OnlineStatusDetails onlineStatus(User user) throws OpenIMServerAPIException {
    return OpenIMUtils.onlineStatus(authService, userService, user.getId());
  }
}
