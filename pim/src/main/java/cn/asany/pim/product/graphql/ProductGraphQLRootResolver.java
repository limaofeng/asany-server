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
package cn.asany.pim.product.graphql;

import cn.asany.pim.product.convert.ProductConverter;
import cn.asany.pim.product.domain.Product;
import cn.asany.pim.product.graphql.input.ProductCreateInput;
import cn.asany.pim.product.graphql.input.ProductUpdateInput;
import cn.asany.pim.product.graphql.input.ProductWhereInput;
import cn.asany.pim.product.graphql.type.ProductConnection;
import cn.asany.pim.product.graphql.type.ProductIdType;
import cn.asany.pim.product.service.ProductService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class ProductGraphQLRootResolver implements GraphQLMutationResolver, GraphQLQueryResolver {

  private final ProductService productService;
  private final ProductConverter productConverter;

  public ProductGraphQLRootResolver(
      ProductService productService, ProductConverter productConverter) {
    this.productService = productService;
    this.productConverter = productConverter;
  }

  public ProductConnection productsConnection(
      ProductWhereInput where, int pageNumber, int pageSize, Sort orderBy) {
    PropertyFilter filter = ObjectUtil.defaultValue(where, ProductWhereInput::new).toFilter();
    Page<Product> page =
        productService.findPage(PageRequest.of(pageNumber - 1, pageSize, orderBy), filter);
    return Kit.connection(page, ProductConnection.class);
  }

  public List<Product> products(ProductWhereInput where, int offset, int limit, Sort sort) {
    PropertyFilter filter = ObjectUtil.defaultValue(where, ProductWhereInput::new).toFilter();
    return productService.findAll(filter, offset, limit, sort);
  }

  public Optional<Product> product(String id, ProductIdType type) {
    if (type == ProductIdType.ID) {
      return productService.findById(Long.parseLong(id));
    } else if (type == ProductIdType.SN) {
      return productService.findBySn(id);
    }
    return Optional.empty();
  }

  public Product createProduct(ProductCreateInput input) {
    Product product = productConverter.toProduct(input);
    return this.productService.save(product);
  }

  public Product updateProduct(Long id, ProductUpdateInput input, Boolean merge) {
    Product product = productConverter.toProduct(input);
    return this.productService.update(id, product, merge);
  }

  public Optional<Product> deleteProduct(Long id) {
    return this.productService.delete(id);
  }

  public Optional<Product> addArticlesToProduct(
      Long productId, String linkType, List<Long> articleIds) {
    return this.productService.addArticlesToProduct(productId, linkType, articleIds);
  }

  public Optional<Product> removeArticlesFromProduct(
      Long productId, String linkType, List<Long> articleIds) {
    return this.productService.removeArticlesFromProduct(productId, linkType, articleIds);
  }
}
