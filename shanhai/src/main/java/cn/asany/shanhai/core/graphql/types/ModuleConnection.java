package cn.asany.shanhai.core.graphql.types;

import cn.asany.shanhai.core.domain.Module;
import java.util.List;
import lombok.*;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

@Data
@EqualsAndHashCode(callSuper = false)
public class ModuleConnection extends BaseConnection<ModuleConnection.ModuleEdge, Module> {
  private List<ModuleConnection.ModuleEdge> edges;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ModuleEdge implements Edge<Module> {
    private String cursor;
    private Module node;
  }
}
