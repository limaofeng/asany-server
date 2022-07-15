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
  private String name;
  private String title;
  private String description;
  private ComponentType type;
  private String template;
  private List<ComponentData> blocks;
  private List<String> tags;
  private Long libraryId;
}
