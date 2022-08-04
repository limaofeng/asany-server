package cn.asany.cms.permission.service;

import cn.asany.cms.permission.domain.enums.SecurityType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SecurityScope implements Serializable {
  private SecurityType type;
  private String value;

  public static SecurityScope newInstance(String id) {
    List<String> strs = new ArrayList<>(Arrays.asList(id.split("_")));
    String type = strs.remove(0);
    String value = String.join("_", strs.toArray(new String[strs.size()]));
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
