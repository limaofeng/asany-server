package cn.asany.im.auth.service.vo;

import cn.asany.im.utils.GeneralRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class UserTokenRequestBody extends GeneralRequest {
  private String secret;
  private Integer platform;
  private String userID;
}
