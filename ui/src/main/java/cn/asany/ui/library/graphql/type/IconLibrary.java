package cn.asany.ui.library.graphql.type;

import cn.asany.ui.resources.domain.Icon;
import java.util.Set;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class IconLibrary implements ILibrary {

  private Long id;
  /** 名称 */
  private String name;
  /** 描述 */
  private String description;
  /** 图标 */
  private Set<Icon> icons;
}
