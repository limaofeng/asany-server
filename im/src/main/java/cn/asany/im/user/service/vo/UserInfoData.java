package cn.asany.im.user.service.vo;

import lombok.Data;

@Data
public class UserInfoData {
  private Integer appMangerLevel;
  private Integer birth;
  private Integer createTime;
  private String email;
  private String ex;
  private String faceURL;
  private Integer gender;
  private Integer globalRecvMsgOpt;
  private String nickname;
  private String phoneNumber;
  private String userID;
}
