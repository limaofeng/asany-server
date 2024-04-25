/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.pim.product.graphql.resolver;

import cn.asany.cms.article.domain.Article;
import cn.asany.cms.article.graphql.input.ArticleWhereInput;
import cn.asany.pim.product.domain.Product;
import cn.asany.pim.product.domain.ProductImage;
import cn.asany.pim.product.service.ProductArticleService;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class ProductGraphQLResolver implements GraphQLResolver<Product> {

  private final ProductArticleService productArticleService;

  public ProductGraphQLResolver(ProductArticleService productArticleService) {
    this.productArticleService = productArticleService;
  }

  public List<Article> articles(
      Product product,
      String linkageType,
      ArticleWhereInput where,
      int offset,
      int limit,
      Sort orderBy) {
    return productArticleService.findArticles(
        product.getId(), linkageType, where, offset, limit, orderBy);
  }

  public List<ProductImage> images(Product product) {
    if (product.getImages() == null) {
      return Collections.emptyList();
    }
    return product.getImages();
  }
}
