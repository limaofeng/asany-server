package cn.asany.security.core.bean.enums;

/**
 * 资源类型
 *
 * @author limaofeng
 */
public enum ResourceType {

  /** url */
  url("url");

  private final String value;

  ResourceType(String value) {
    this.value = value;
  }

  public String getValue() {
    return this.value;
  }
}
