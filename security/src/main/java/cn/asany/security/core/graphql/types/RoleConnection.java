package cn.asany.security.core.graphql.types;

import cn.asany.security.core.domain.Role;
import java.util.List;
import lombok.*;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

/**
 * 角色查询接口
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RoleConnection extends BaseConnection<RoleConnection.RoleEdge, Role> {

  private List<RoleEdge> edges;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class RoleEdge implements Edge<Role> {
    private String cursor;
    private Role node;
  }
}
