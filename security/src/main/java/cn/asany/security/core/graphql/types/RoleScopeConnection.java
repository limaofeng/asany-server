package cn.asany.security.core.graphql.types;

import cn.asany.security.core.domain.TrustedEntityType;
import java.util.List;
import lombok.*;
import net.asany.jfantasy.graphql.Edge;
import net.asany.jfantasy.graphql.types.BaseConnection;

@Data
@EqualsAndHashCode(callSuper = false)
public class RoleScopeConnection
    extends BaseConnection<RoleScopeConnection.RoleScopeEdge, TrustedEntityType> {

  private List<RoleScopeEdge> edges;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class RoleScopeEdge implements Edge<TrustedEntityType> {
    private String cursor;
    private TrustedEntityType node;
  }
}
