package cn.asany.pim.product.graphql.type;

import cn.asany.pim.product.domain.Product;
import java.util.List;
import lombok.*;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

/**
 * 角色查询接口
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ProductConnection extends BaseConnection<ProductConnection.ProductEdge, Product> {

  private List<ProductEdge> edges;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class ProductEdge implements Edge<Product> {
    private String cursor;
    private Product node;
  }
}
