package cn.asany.security.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordPolicy {
  private int minLength; // 密码最小长度
  private int maxLength; // 密码最大长度
  private boolean containsLowerCase; // 是否要求包含小写字母
  private boolean containsUpperCase; // 是否要求包含大写字母
  private boolean containsDigit; // 是否要求包含数字
  private boolean containsSymbol; // 是否要求包含符号
  private int minDifferentCharacters; // 最小不同字符数量
  private boolean allowUsername; // 是否允许密码中包含用户名
  private int passwordExpiryDays; // 密码有效期（天）
  private boolean limitLoginAfterExpiry; // 密码过期后是否限制登录
  private int maxPasswordAttemptsPerHour; // 一小时内最大错误登录尝试次数
  private int maxHistoryPasswordCheck; // 历史密码检查策略（禁止使用前次数）
}
