package cn.asany.im.auth.graphql.resolvers;

import cn.asany.im.auth.graphql.type.OnlineStatus;
import cn.asany.im.auth.graphql.type.OnlineStatusDetails;
import cn.asany.im.auth.graphql.type.Platform;
import cn.asany.im.auth.service.AuthService;
import cn.asany.im.error.OpenIMServerAPIException;
import cn.asany.im.user.service.UserService;
import cn.asany.im.user.service.vo.GetUsersOnlineStatusRequestBody;
import cn.asany.im.user.service.vo.UsersOnlineStatusData;
import cn.asany.security.auth.graphql.types.CurrentUser;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.List;
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
    String adminToken = this.authService.adminToken();
    List<UsersOnlineStatusData> onlineStatusDataList =
        this.userService.getUsersOnlineStatus(
            adminToken,
            GetUsersOnlineStatusRequestBody.builder().userID(String.valueOf(user.getId())).build());

    if (onlineStatusDataList.isEmpty()) {
      return OnlineStatusDetails.builder()
          .status(OnlineStatus.offline)
          .platformStatus(Platform.iOS, OnlineStatus.online)
          .build();
    }

    UsersOnlineStatusData onlineStatusData = onlineStatusDataList.get(0);

    OnlineStatusDetails.OnlineStatusDetailsBuilder builder =
        OnlineStatusDetails.builder().status(onlineStatusData.getStatus());

    for (UsersOnlineStatusData.DetailPlatformStatus platformStatus :
        onlineStatusData.getDetailPlatformStatus()) {
      builder.platformStatus(platformStatus.getPlatform(), platformStatus.getStatus());
    }

    return builder.build();
  }
}
