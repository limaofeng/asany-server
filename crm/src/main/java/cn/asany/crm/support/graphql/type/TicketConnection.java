package cn.asany.crm.support.graphql.type;

import cn.asany.crm.support.domain.Ticket;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

@Data
@EqualsAndHashCode(callSuper = true)
public class TicketConnection extends BaseConnection<TicketConnection.TicketEdge, Ticket> {

  private List<TicketEdge> edges;

  @Data
  public static class TicketEdge implements Edge<Ticket> {
    private String cursor;
    private Ticket node;
  }
}
