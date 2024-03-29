package cn.asany.crm.support.graphql.input;

import cn.asany.crm.support.domain.enums.TicketTargetType;
import lombok.Data;

@Data
public class TicketTargetInput {
  private Long id;
  private TicketTargetType type;
}
