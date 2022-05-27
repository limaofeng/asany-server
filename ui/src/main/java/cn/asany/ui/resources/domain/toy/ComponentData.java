package cn.asany.ui.resources.domain.toy;

import java.io.Serializable;
import java.util.Map;
import lombok.Data;

@Data
public class ComponentData implements Serializable {
  private String key;
  private Map<String, Object> props;
}
