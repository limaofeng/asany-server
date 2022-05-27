package cn.asany.ui.resources.graphql.type;

import cn.asany.ui.resources.domain.enums.ComponentType;
import cn.asany.ui.resources.domain.toy.ComponentData;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComponentCreateInput {
  private String code;
  private String name;
  private ComponentType type;
  private String template;
  private List<ComponentData> blocks;
}
