package cn.asany.security.core.graphql.types;

import cn.asany.security.core.domain.PermissionPolicy;
import java.util.List;
import lombok.*;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

/**
 * 权限策略 Connection
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PermissionPolicyConnection
    extends BaseConnection<PermissionPolicyConnection.PermissionPolicyEdge, PermissionPolicy> {

  private List<PermissionPolicyEdge> edges;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PermissionPolicyEdge implements Edge<PermissionPolicy> {
    private String cursor;
    private PermissionPolicy node;
  }
}
