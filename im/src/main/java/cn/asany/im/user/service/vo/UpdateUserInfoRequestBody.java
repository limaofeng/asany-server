package cn.asany.im.user.service.vo;

import cn.asany.im.utils.GeneralRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class UpdateUserInfoRequestBody extends GeneralRequest {
  private Integer birth;
  private String email;
  private String ex;
  private String faceURL;
  private Integer gender;
  private String nickname;
  private String phoneNumber;
  private String userID;
}
