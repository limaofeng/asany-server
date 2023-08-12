package cn.asany.security.core.graphql.types;

import cn.asany.security.core.domain.Group;
import java.util.List;
import lombok.*;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

/**
 * 用户组连接
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class GroupConnection extends BaseConnection<GroupConnection.GroupEdge, Group> {

  private List<GroupEdge> edges;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class GroupEdge implements Edge<Group> {
    private String cursor;
    private Group node;
  }
}
