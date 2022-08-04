package cn.asany.im.auth.service.vo;

import cn.asany.im.utils.GeneralRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class UserRegisterRequestBody extends GeneralRequest {
  private Long birth;
  private String email;
  private String faceURL;
  private Integer gender;
  private String nickname;
  private String phoneNumber;
  private Integer platform;
  private String secret;
  private String userID;
  private String ex;
}
