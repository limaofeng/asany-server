package cn.asany.email.mailbox.graphql.type;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

/**
 * 邮件分页对象
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class MailboxMessageConnection
    extends BaseConnection<MailboxMessageConnection.MailboxMessageEdge> {

  private List<MailboxMessageConnection.MailboxMessageEdge> edges;

  @Data
  public static class MailboxMessageEdge implements Edge<MailboxMessageResult> {
    private String cursor;
    private MailboxMessageResult node;

    public MailboxMessageEdge(MailboxMessageResult node) {
      this.node = node;
    }
  }
}
