package cn.asany.security.core.bean.enums;

/** 权限类型 */
public enum PermissionType {
  /** 允许直接访问 */
  any("允许直接访问"),
  /** 通配符 */
  antPath("通配符"),
  /** Ip地址 */
  ipAddress("Ip地址"),
  /** 正则表达式 */
  regex("正则表达式"),
  /** */
  and("多组条件同时满足"),
  /** 多条件满足一个 */
  or("多条件满足一个");

  private String value;

  PermissionType(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
