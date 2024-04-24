package cn.asany.message.data.graphql.type;

import cn.asany.message.data.domain.Message;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.graphql.Edge;
import net.asany.jfantasy.graphql.types.BaseConnection;

/**
 * Message Connection
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MessageConnection extends BaseConnection<MessageConnection.MessageEdge, Message> {
  private List<MessageEdge> edges;

  @Data
  public static class MessageEdge implements Edge<Message> {
    private String cursor;
    private Message node;
  }
}
