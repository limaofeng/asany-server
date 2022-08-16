package cn.asany.im.auth.graphql.type;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OnlineStatusDetails {
  private OnlineStatus status;
  private List<DetailPlatformStatus> detailPlatformStatus;

  public static class OnlineStatusDetailsBuilder {

    public OnlineStatusDetailsBuilder() {
      this.detailPlatformStatus = new ArrayList<>();
    }

    public OnlineStatusDetailsBuilder platformStatus(Platform platform, OnlineStatus status) {
      this.detailPlatformStatus.add(
          DetailPlatformStatus.builder().platform(platform).status(status).build());
      return this;
    }
  }
}
