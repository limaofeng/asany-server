package cn.asany.pim.product.graphql.input;

import java.util.List;
import lombok.Data;

@Data
public class ProductCreateInput {
  private String brandId;
  private String name;
  private List<ProductImageInput> images;
}
