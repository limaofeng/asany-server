package cn.asany.im.user.graphql.resolvers;

import cn.asany.im.auth.graphql.type.OnlineStatus;
import cn.asany.im.auth.graphql.type.OnlineStatusDetails;
import cn.asany.im.auth.graphql.type.Platform;
import cn.asany.im.auth.service.AuthService;
import cn.asany.im.error.OpenIMServerAPIException;
import cn.asany.im.user.service.UserService;
import cn.asany.im.utils.OpenIMUtils;
import cn.asany.security.core.domain.User;
import graphql.kickstart.tools.GraphQLResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("im.user.UserGraphQLResolver")
public class UserGraphQLResolver implements GraphQLResolver<User> {

  private final AuthService authService;
  private final UserService userService;

  public UserGraphQLResolver(AuthService authService, UserService userService) {
    this.authService = authService;
    this.userService = userService;
  }

  public String imToken(User user, Platform platform) throws OpenIMServerAPIException {
    try {
    return this.authService.token(platform, String.valueOf(user.getId()));
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return "";
    }
  }

  public OnlineStatusDetails onlineStatus(User user) throws OpenIMServerAPIException {
    try {
    return OpenIMUtils.onlineStatus(authService, userService, user.getId());
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      return OnlineStatusDetails.builder().status(OnlineStatus.offline).build();
    }
  }
}
