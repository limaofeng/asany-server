package cn.asany.organization.core.domain.enums;

/**
 * 组织成员角色
 *
 * @author limaofeng
 */
public enum MemberRole {
  ADMIN("admin"),
  USER("user");

  private String value;

  MemberRole(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
