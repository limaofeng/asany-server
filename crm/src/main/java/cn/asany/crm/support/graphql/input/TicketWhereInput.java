package cn.asany.crm.support.graphql.input;

import cn.asany.crm.support.domain.Ticket;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.graphql.inputs.WhereInput;

@Data
@EqualsAndHashCode(callSuper = true)
public class TicketWhereInput extends WhereInput<TicketWhereInput, Ticket> {

  public void setKeyword(String keyword) {
    filter.or(
        PropertyFilter.newFilter().contains("description", keyword),
        PropertyFilter.newFilter().contains("no", keyword));
  }

  public void setCustomerId(Long customerId) {
    filter.equal("customer.id", customerId);
  }

  public void setStoreId(Long storeId) {
    filter.equal("store.id", storeId);
  }

  public void setTarget(TicketTargetInput target) {
    filter.equal("target.id", target.getId()).equal("target.type", target.getType());
  }

  public void setAssignee(Long assigneeId) {
    filter.equal("assignee.id", assigneeId);
  }

  public void setType(Long typeId) {
    filter.equal("type.id", typeId);
  }
}
