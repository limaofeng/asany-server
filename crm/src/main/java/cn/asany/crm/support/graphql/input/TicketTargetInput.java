package cn.asany.crm.support.graphql.input;

import cn.asany.base.common.TicketTargetType;
import lombok.Data;

@Data
public class TicketTargetInput {
  private Long id;
  private TicketTargetType type;
  private String name;
}
