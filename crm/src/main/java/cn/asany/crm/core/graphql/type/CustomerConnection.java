package cn.asany.crm.core.graphql.type;

import cn.asany.crm.core.domain.Customer;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerConnection extends BaseConnection<CustomerConnection.CustomerEdge, Customer> {
  private List<CustomerConnection.CustomerEdge> edges;

  @Data
  public static class CustomerEdge implements Edge<Customer> {
    private String cursor;
    private Customer node;
  }
}
