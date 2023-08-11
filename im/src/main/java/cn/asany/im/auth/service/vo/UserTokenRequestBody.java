package cn.asany.im.auth.service.vo;

import cn.asany.im.utils.GeneralRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class UserTokenRequestBody extends GeneralRequest {
  private String secret;

  @JsonProperty("platformID")
  private Integer platform;

  @JsonProperty("userID")
  private String user;
}
