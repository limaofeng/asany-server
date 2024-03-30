package cn.asany.crm.support.graphql.resolver;

import cn.asany.base.common.TicketTarget;
import cn.asany.base.common.TicketTargetResolver;
import cn.asany.crm.support.domain.Ticket;
import cn.asany.storage.api.FileObject;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class TicketGraphQLResolver implements GraphQLResolver<Ticket> {

  private final TicketTargetResolver ticketTargetResolver;

  public TicketGraphQLResolver(TicketTargetResolver ticketTargetResolver) {
    this.ticketTargetResolver = ticketTargetResolver;
  }

  public List<FileObject> attachments(Ticket ticket) {
    if (ticket.getAttachments() == null) {
      return Collections.emptyList();
    }
    return ticket.getAttachments();
  }

  public Optional<TicketTarget> target(Ticket ticket) {
    if (ticket.getTarget() == null) {
      return Optional.empty();
    }
    return ticketTargetResolver.resolve(ticket.getTarget().getType(), ticket.getTarget().getId());
  }
}
