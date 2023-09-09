package cn.asany.weixin.domain;

import java.io.Serializable;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserKey implements Serializable {

  public static UserKey newInstance(String appId, String openId) {
    return new UserKey(appId, openId);
  }

  // APPID
  @Column(name = "APPID", updatable = false)
  private String appId;
  // 用户的标识，对当前公众号唯一
  @Column(name = "OPENID", updatable = false)
  private String openId;
}
