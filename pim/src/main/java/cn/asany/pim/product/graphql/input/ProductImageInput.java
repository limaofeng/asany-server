package cn.asany.pim.product.graphql.input;

import cn.asany.storage.api.FileObject;
import lombok.Data;

@Data
public class ProductImageInput {
  private Long id;
  private String url;
  private String alt;
  private String title;
  private String description;
  private FileObject image;
}
