package cn.asany.im.auth.graphql.type;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetailPlatformStatus {
  private Platform platform;
  private OnlineStatus status;
}
