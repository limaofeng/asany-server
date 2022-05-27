package cn.asany.security.core.domain.enums;

/** @author limaofeng */
public enum UserType {
  /** 个人用户 */
  USER("个人"),
  /** 管理员 */
  ADMIN("管理员");

  private String value;

  UserType(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
