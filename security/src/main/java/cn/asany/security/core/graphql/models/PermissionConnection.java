package cn.asany.security.core.graphql.models;

import cn.asany.security.core.bean.Permission;
import java.util.List;
import lombok.*;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

/**
 * @author: guoyong
 * @description: 权限接口
 * @create: 2020/6/9 15:47
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class PermissionConnection
    extends BaseConnection<PermissionConnection.PermissionEdge, Permission> {

  private List<PermissionEdge> edges;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class PermissionEdge implements Edge<Permission> {
    private String cursor;
    private Permission node;
  }
}
