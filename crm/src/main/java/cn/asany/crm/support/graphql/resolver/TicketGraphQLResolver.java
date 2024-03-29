package cn.asany.crm.support.graphql.resolver;

import cn.asany.crm.support.domain.Ticket;
import cn.asany.storage.api.FileObject;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class TicketGraphQLResolver implements GraphQLResolver<Ticket> {

  public List<FileObject> attachments(Ticket ticket) {
    if (ticket.getAttachments() == null) {
      return Collections.emptyList();
    }
    return ticket.getAttachments();
  }

}
