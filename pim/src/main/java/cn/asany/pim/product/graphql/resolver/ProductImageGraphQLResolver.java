package cn.asany.pim.product.graphql.resolver;

import cn.asany.pim.product.domain.ProductImage;
import graphql.kickstart.tools.GraphQLResolver;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Component;

@Component
public class ProductImageGraphQLResolver implements GraphQLResolver<ProductImage> {

  public String url(ProductImage image) {
    if (StringUtil.isNotBlank(image.getUrl())) {
      return image.getUrl();
    }
    return image.getImage().getPath();
  }
}
