package cn.asany.im.auth.service.vo;

import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Data
@Builder
public class UserRegisterRequestBody {
  private String secret;
  private List<UserItem> users;

  public static class UserRegisterRequestBodyBuilder {
    public UserRegisterRequestBodyBuilder addUser(String user, String nickname, String faceUrl) {
      if (this.users == null) {
        this.users = new ArrayList<>();
      }
      this.users.add(UserItem.builder().userID(user).nickname(nickname).faceURL(faceUrl).build());
      return this;
    }
  }

  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UserItem {
    /** 用户名 */
    private String nickname;
    /** 用户ID */
    private String userID;
    /** 用户头像 */
    private String faceURL;
  }
}
