package cn.asany.crm.core.graphql.type;

import cn.asany.crm.core.domain.CustomerStore;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.graphql.Edge;
import net.asany.jfantasy.graphql.types.BaseConnection;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomerStoreConnection
    extends BaseConnection<CustomerStoreConnection.CustomerStoreEdge, CustomerStore> {
  private List<CustomerStoreEdge> edges;

  @Data
  public static class CustomerStoreEdge implements Edge<CustomerStore> {
    private String cursor;
    private CustomerStore node;
  }
}
