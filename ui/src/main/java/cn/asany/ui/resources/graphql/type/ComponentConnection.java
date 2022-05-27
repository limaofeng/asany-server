package cn.asany.ui.resources.graphql.type;

import cn.asany.ui.resources.domain.Component;
import java.util.List;
import lombok.*;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

/**
 * 组件 分页对象
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ComponentConnection
    extends BaseConnection<ComponentConnection.ComponentEdge, Component> {
  private List<ComponentEdge> edges;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ComponentEdge implements Edge<Component> {
    private String cursor;
    private Component node;
  }
}
