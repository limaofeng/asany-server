package cn.asany.im.auth.graphql.resolvers;

import cn.asany.im.auth.graphql.type.OnlineStatusDetails;
import cn.asany.im.auth.graphql.type.Platform;
import cn.asany.im.auth.service.AuthService;
import cn.asany.im.error.OpenIMServerAPIException;
import cn.asany.im.user.service.UserService;
import cn.asany.im.utils.OpenIMUtils;
import cn.asany.security.auth.graphql.types.CurrentUser;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

@Component("im.auth.CurrentUserGraphQLResolver")
public class CurrentUserGraphQLResolver implements GraphQLResolver<CurrentUser> {

  private final AuthService authService;
  private final UserService userService;

  public CurrentUserGraphQLResolver(AuthService authService, UserService userService) {
    this.authService = authService;
    this.userService = userService;
  }

  public String imToken(CurrentUser user, Platform platform) throws OpenIMServerAPIException {
    return this.authService.token(platform, String.valueOf(user.getId()));
  }

  public OnlineStatusDetails onlineStatus(CurrentUser user) throws OpenIMServerAPIException {
    return OpenIMUtils.onlineStatus(authService, userService, user.getId());
  }
}