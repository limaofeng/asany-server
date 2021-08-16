package cn.asany.security.core.bean.enums;

public enum Sex {
  /** 男 */
  male("男"),
  /** 女 */
  female("女"),
  /** 未知 */
  unknown("未知");

  private String value;

  Sex(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
