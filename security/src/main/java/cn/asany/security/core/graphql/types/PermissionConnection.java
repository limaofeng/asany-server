package cn.asany.security.core.graphql.types;

import cn.asany.security.core.domain.PermissionStatement;
import java.util.List;
import lombok.*;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

/**
 * 权限 Connection
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PermissionConnection
    extends BaseConnection<PermissionConnection.PermissionEdge, PermissionStatement> {

  private List<PermissionEdge> edges;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PermissionEdge implements Edge<PermissionStatement> {
    private String cursor;
    private PermissionStatement node;
  }
}
