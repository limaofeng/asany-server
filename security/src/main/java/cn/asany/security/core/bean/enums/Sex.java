package cn.asany.security.core.bean.enums;

/**
 * 性别枚举
 *
 * @author limaofeng
 */
public enum Sex {
  /** 男 */
  male("男"),
  /** 女 */
  female("女"),
  /** 未知 */
  unknown("未知");

  private final String value;

  Sex(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
