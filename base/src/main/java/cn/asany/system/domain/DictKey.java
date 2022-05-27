package cn.asany.system.domain;

import java.io.Serializable;
import javax.persistence.Column;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 数据字典 Key
 *
 * @author limaofeng
 */
@Data
public class DictKey implements Serializable {

  public static DictKey newInstance(String code, String type) {
    return new DictKey(code, type);
  }

  /** 代码 */
  @Column(name = "CODE", length = 50)
  private String code;

  /** 配置类别 */
  @Column(name = "TYPE", length = 20)
  private String type;

  public DictKey() {}

  public static DictKey newInstance(String key) {
    return new DictKey(key);
  }

  public DictKey(String key) {
    this.type = key.split(":")[0];
    this.code = key.split(":")[1];
  }

  public DictKey(String code, String type) {
    super();
    this.code = code;
    this.type = type;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .appendSuper(super.hashCode())
        .append(this.getType())
        .append(this.getCode())
        .toHashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof DictKey) {
      DictKey key = (DictKey) o;
      return new EqualsBuilder()
          .appendSuper(super.equals(o))
          .append(this.getType(), key.getType())
          .append(this.getCode(), key.getCode())
          .isEquals();
    }
    return false;
  }

  @Override
  public String toString() {
    return this.type + ":" + this.code;
  }
}
