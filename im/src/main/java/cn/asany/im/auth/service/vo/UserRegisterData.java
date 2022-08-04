package cn.asany.im.auth.service.vo;

import lombok.Data;

@Data
public class UserRegisterData {
  private int expiredTime;
  private String token;
  private String userID;
}
