package cn.asany.ui.resources.bean.toy;

import java.util.Map;
import lombok.Data;

@Data
public class ComponentData {
  private String key;
  private Map<String, Object> props;
}
