package cn.asany.ui.library.graphql.type;

import cn.asany.ui.resources.domain.Component;
import java.util.List;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ComponentLibrary implements ILibrary {

  private Long id;
  /** 名称 */
  private String name;
  /** 描述 */
  private String description;
  /** 组件 */
  private List<Component> components;
}
