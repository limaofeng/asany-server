package cn.asany.im.auth.graphql.resolvers;

import cn.asany.im.auth.graphql.type.OnlineStatus;
import cn.asany.im.auth.graphql.type.OnlineStatusDetails;
import cn.asany.im.auth.graphql.type.Platform;
import cn.asany.im.auth.service.AuthService;
import cn.asany.im.error.OpenIMServerAPIException;
import cn.asany.im.user.service.UserService;
import cn.asany.im.utils.OpenIMUtils;
import cn.asany.security.auth.graphql.types.CurrentUser;
import graphql.kickstart.tools.GraphQLResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 当前用户 GraphQL Resolver
 *
 * @author limaofeng
 */
@Slf4j
@Component("im.auth.CurrentUserGraphQLResolver")
public class CurrentUserGraphQLResolver implements GraphQLResolver<CurrentUser> {

  private final AuthService authService;
  private final UserService userService;

  public CurrentUserGraphQLResolver(AuthService authService, UserService userService) {
    this.authService = authService;
    this.userService = userService;
  }

  public String imToken(CurrentUser user, Platform platform) {
    try {
      return this.authService.token(platform, String.valueOf(user.getId()));
    } catch (OpenIMServerAPIException e) {
      log.error(e.getMessage(), e);
      return null;
    }
  }

  public OnlineStatusDetails onlineStatus(CurrentUser user) {
    try {
      return OpenIMUtils.onlineStatus(authService, userService, user.getId());
    } catch (OpenIMServerAPIException e) {
      log.error(e.getMessage(), e);
      return OnlineStatusDetails.builder().status(OnlineStatus.offline).build();
    }
  }
}
