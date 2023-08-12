package cn.asany.security.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 访问控制
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccessControlSettings {
  /**
   * 密码规则
   */
  private PasswordPolicy passwordPolicy;
  /**
   * 用户安全设置
   */
  private UserSecuritySettings userSecuritySettings;
}
