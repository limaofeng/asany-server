package cn.asany.security.core.graphql.types;

import cn.asany.security.core.domain.Tenant;
import java.util.List;
import lombok.*;
import net.asany.jfantasy.graphql.Edge;
import net.asany.jfantasy.graphql.types.BaseConnection;

@Data
@EqualsAndHashCode(callSuper = false)
public class TenantConnection extends BaseConnection<TenantConnection.TenantEdge, Tenant> {

  private List<TenantConnection.TenantEdge> edges;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class TenantEdge implements Edge<Tenant> {
    private String cursor;
    private Tenant node;
  }
}
