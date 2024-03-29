package cn.asany.crm.support.convert;

import cn.asany.crm.support.domain.Ticket;
import cn.asany.crm.support.graphql.input.TicketCreateInput;
import cn.asany.crm.support.graphql.input.TicketUpdateInput;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface TicketConverter {
  Ticket toTicket(TicketCreateInput input);

  Ticket toTicket(TicketUpdateInput input);
}
