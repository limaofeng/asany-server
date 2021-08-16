package cn.asany.security.core.graphql.models;

import cn.asany.security.core.bean.RoleScope;
import java.util.List;
import lombok.*;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

@Data
@EqualsAndHashCode(callSuper = false)
public class RoleScopeConnection extends BaseConnection<RoleScopeConnection.RoleScopeEdge> {

  private List<RoleScopeEdge> edges;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class RoleScopeEdge implements Edge<RoleScope> {
    private String cursor;
    private RoleScope node;
  }
}
