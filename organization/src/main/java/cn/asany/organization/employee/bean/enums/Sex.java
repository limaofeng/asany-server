package cn.asany.organization.employee.bean.enums;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-03-11 14:24
 */
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
