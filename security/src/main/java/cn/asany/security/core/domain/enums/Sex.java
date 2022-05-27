package cn.asany.security.core.domain.enums;

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

  public String value() {
    return this.value;
  }

  public static Sex of(String value) {
    if (Sex.male.value().equals(value)) {
      return Sex.male;
    }
    if (Sex.female.value().equals(value)) {
      return Sex.female;
    }
    return Sex.unknown;
  }
}
