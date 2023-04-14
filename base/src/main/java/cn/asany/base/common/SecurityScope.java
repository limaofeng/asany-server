package cn.asany.base.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 权限范围
 *
 * @author limaofeng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecurityScope implements Serializable {
  private SecurityType type;
  private String value;

  public static SecurityScope newInstance(String id) {
    List<String> strings = new ArrayList<>(Arrays.asList(id.split("_")));
    String type = strings.remove(0);
    String value = String.join("_", strings.toArray(new String[0]));
    if ("EMPLOYEEGROUP".equals(type)) {
      return new SecurityScope(SecurityType.employeeGroup, value);
    }
    return new SecurityScope(SecurityType.valueOf(type.toLowerCase()), value);
  }

  public static SecurityScope newInstance(SecurityType type, String value) {
    return new SecurityScope(type, value);
  }

  @Override
  public String toString() {
    return type.name().toUpperCase() + "_" + value;
  }
}
