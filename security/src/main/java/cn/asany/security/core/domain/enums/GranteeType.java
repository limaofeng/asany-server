package cn.asany.security.core.domain.enums;

import org.jfantasy.framework.error.ValidationException;

/**
 * 被授权者类型
 *
 * @author limaofeng
 */
public enum GranteeType {
  /** 用户 */
  USER("user"),
  /** 用户组 */
  GROUP("group"),
  /** 角色 */
  ROLE("role");

  public static final String DELIMITER = ":";

  private final String name;

  GranteeType(String name) {
    this.name = name;
  }

  public static GranteeType of(String value) {
    if (value.startsWith(USER.getName() + DELIMITER)) {
      return USER;
    } else if (value.startsWith(GROUP.getName() + DELIMITER)) {
      return GROUP;
    } else if (value.startsWith(ROLE.getName() + DELIMITER)) {
      return ROLE;
    }
    throw new ValidationException("被授权者类型格式错误：" + value);
  }

  public String getName() {
    return name;
  }
}
