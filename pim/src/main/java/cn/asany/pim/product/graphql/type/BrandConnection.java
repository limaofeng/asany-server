package cn.asany.pim.product.graphql.type;

import cn.asany.pim.product.domain.Brand;
import java.util.List;
import lombok.*;
import org.jfantasy.graphql.Edge;
import org.jfantasy.graphql.types.BaseConnection;

@Data
@EqualsAndHashCode(callSuper = false)
public class BrandConnection extends BaseConnection<BrandConnection.BrandEdge, Brand> {

  private List<BrandConnection.BrandEdge> edges;

  @Data
  @Builder
  @NoArgsConstructor
  @AllArgsConstructor
  public static class BrandEdge implements Edge<Brand> {
    private String cursor;
    private Brand node;
  }
}
