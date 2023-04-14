package cn.asany.im.utils;

import cn.asany.im.auth.graphql.type.OnlineStatus;
import cn.asany.im.auth.graphql.type.OnlineStatusDetails;
import cn.asany.im.auth.graphql.type.Platform;
import cn.asany.im.auth.service.AuthService;
import cn.asany.im.error.OpenIMServerAPIException;
import cn.asany.im.user.service.UserService;
import cn.asany.im.user.service.vo.GetUsersOnlineStatusRequestBody;
import cn.asany.im.user.service.vo.UsersOnlineStatusData;
import java.util.List;

/**
 * OpenIM 工具类
 *
 * @author limaofeng
 */
public class OpenIMUtils {

  public static <T> T getData(GeneralResponse<T> responseBody) throws OpenIMServerAPIException {
    if (responseBody.getErrCode() != 0) {
      throw new OpenIMServerAPIException(responseBody.getErrCode(), responseBody.getErrMsg());
    }
    return responseBody.getData();
  }

  public static OnlineStatusDetails onlineStatus(
      AuthService authService, UserService userService, Long user) throws OpenIMServerAPIException {
    String adminToken = authService.adminToken();
    List<UsersOnlineStatusData> onlineStatusDataList =
        userService.getUsersOnlineStatus(
            adminToken,
            GetUsersOnlineStatusRequestBody.builder().userID(String.valueOf(user)).build());

    if (onlineStatusDataList.isEmpty()) {
      return OnlineStatusDetails.builder()
          .status(OnlineStatus.offline)
          .platformStatus(Platform.IOS, OnlineStatus.online)
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
