package cn.asany.security.core.graphql.input;

import lombok.Data;

@Data
public class UserSettingsInput {
  /** 为空会自动设置密码 */
  private String password;
  /** 在下次登录时必须重置密码 */
  private boolean forcePasswordReset;
}
