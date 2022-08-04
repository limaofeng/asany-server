package cn.asany.im.auth.service.vo;

import lombok.Data;

@Data
public class UserTokenData {
  private long expiredTime;
  private String token;
  private String userID;
}
