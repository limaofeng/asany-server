package cn.asany.crm.support.graphql.input;

import cn.asany.crm.support.domain.enums.TicketPriority;
import lombok.Data;

@Data
public class TicketUpdateInput {
  /** 工单描述 */
  private String description;

  /** 工单优先级 */
  private TicketPriority priority;
}
