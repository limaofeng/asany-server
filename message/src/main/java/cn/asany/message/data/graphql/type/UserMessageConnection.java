package cn.asany.message.data.graphql.type;

import cn.asany.message.data.domain.UserMessage;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

/**
 * Message Connection
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class UserMessageConnection
    extends BaseConnection<UserMessageConnection.UserMessageEdge, UserMessage> {
  private List<UserMessageEdge> edges;

  @Data
  public static class UserMessageEdge implements Edge<UserMessage> {
    private String cursor;
    private UserMessage node;
  }
}
