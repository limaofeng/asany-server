package cn.asany.im.user.service.vo;

import cn.asany.im.auth.graphql.type.OnlineStatus;
import cn.asany.im.auth.graphql.type.Platform;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersOnlineStatusData {
  @Builder.Default private List<DetailPlatformStatus> detailPlatformStatus = new ArrayList<>();
  private OnlineStatus status;
  private String userID;

  @Data
  public static class DetailPlatformStatus {
    private Platform platform;
    private OnlineStatus status;
  }
}
