package cn.asany.security.core.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class PasswordPolicy {
  /** 密码最小长度 */
  @Column(name = "MIN_LENGTH", nullable = false)
  private Integer minLength;

  /** 密码最大长度 */
  @Column(name = "MAX_LENGTH", nullable = false)
  private Integer maxLength;

  /** 密码中必须包含小写字母 */
  @Column(name = "REQUIRES_LOWER_CASE", nullable = false)
  private Boolean requiresLowerCase;

  /** 密码中必须包含大写字母 */
  @Column(name = "REQUIRES_UPPER_CASE", nullable = false)
  private Boolean requiresUpperCase;

  /** 密码中必须包含数字 */
  @Column(name = "REQUIRES_DIGIT", nullable = false)
  private Boolean requiresDigit;

  /** 密码中必须包含符号 */
  @Column(name = "REQUIRES_SYMBOL", nullable = false)
  private Boolean requiresSymbol;

  /** 最小不同字符数量 */
  @Column(name = "MIN_UNIQUE_CHARACTERS", nullable = false)
  private Integer minUniqueCharacters;

  /** 是否允许密码中包含用户名 */
  @Column(name = "ALLOW_USERNAME_IN_PASSWORD", nullable = false)
  private Boolean allowUsernameInPassword;

  /** 密码有效期（天） */
  @Column(name = "PASSWORD_VALIDITY_DAYS", nullable = false)
  private Integer passwordValidityDays;

  /** 密码过期后是否限制登录 */
  @Column(name = "RESTRICT_LOGIN_AFTER_EXPIRY", nullable = false)
  private Boolean restrictLoginAfterExpiry;

  /** 一小时内最大错误登录尝试次数 */
  @Column(name = "MAX_LOGIN_ATTEMPTS_PER_HOUR", nullable = false)
  private Integer maxLoginAttemptsPerHour;

  /** 历史密码检查策略（禁止使用前次数） */
  @Column(name = "MAX_PROHIBITED_PASSWORD_HISTORY", nullable = false)
  private Integer maxProhibitedPasswordHistory;
}
