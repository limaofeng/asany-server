package cn.asany.crm.support.convert;

import cn.asany.crm.support.domain.Ticket;
import cn.asany.crm.support.graphql.input.TicketCreateInput;
import cn.asany.crm.support.graphql.input.TicketUpdateInput;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    builder = @Builder,
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface TicketConverter {

  @Mappings({
    @Mapping(source = "type", target = "type.id"),
    @Mapping(source = "storeId", target = "store.id"),
    @Mapping(source = "customerId", target = "customer.id"),
  })
  Ticket toTicket(TicketCreateInput input);

  Ticket toTicket(TicketUpdateInput input);
}
