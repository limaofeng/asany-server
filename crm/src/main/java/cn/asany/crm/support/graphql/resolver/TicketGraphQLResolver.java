package cn.asany.crm.support.graphql.resolver;

import cn.asany.base.common.TicketTarget;
import cn.asany.base.common.TicketTargetResolver;
import cn.asany.crm.support.domain.Ticket;
import cn.asany.crm.support.domain.TicketStatusLog;
import cn.asany.crm.support.domain.enums.TicketStatus;
import cn.asany.storage.api.FileObject;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.Collections;
import java.util.Date;
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

  public List<TicketStatusLog> statusHistory(Ticket ticket) {
    if (ticket.getStatusHistory() == null) {
      return Collections.emptyList();
    }
    return ticket.getStatusHistory();
  }

  public Optional<Date> completedAt(Ticket ticket) {
    List<TicketStatusLog> ticketChanges = ticket.getStatusHistory();
    if (ticketChanges == null) {
      return Optional.empty();
    }
    return ticketChanges.stream()
        .filter(ticketChange -> ticketChange.getStatus().equals(TicketStatus.RESOLVED))
        .findFirst()
        .map(TicketStatusLog::getLogTime);
  }

  public Optional<Date> suspendedAt(Ticket ticket) {
    List<TicketStatusLog> ticketChanges = ticket.getStatusHistory();
    if (ticketChanges == null) {
      return Optional.empty();
    }
    return ticketChanges.stream()
        .filter(ticketChange -> ticketChange.getStatus().equals(TicketStatus.SUSPENDED))
        .findFirst()
        .map(TicketStatusLog::getLogTime);
  }
}
