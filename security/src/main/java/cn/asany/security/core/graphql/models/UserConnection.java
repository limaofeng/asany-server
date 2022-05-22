package cn.asany.security.core.graphql.models;

import cn.asany.security.core.bean.User;
import java.util.List;
import lombok.*;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserConnection extends BaseConnection<UserConnection.UserEdge, User> {

  private List<UserEdge> edges;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class UserEdge implements Edge<User> {
    private String cursor;
    private User node;
  }
}
