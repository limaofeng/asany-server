package cn.asany.security.core.graphql.types;

import cn.asany.security.core.domain.RoleSpace;
import java.util.List;
import lombok.*;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

@Data
@EqualsAndHashCode(callSuper = false)
public class RoleScopeConnection
    extends BaseConnection<RoleScopeConnection.RoleScopeEdge, RoleSpace> {

  private List<RoleScopeEdge> edges;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class RoleScopeEdge implements Edge<RoleSpace> {
    private String cursor;
    private RoleSpace node;
  }
}
