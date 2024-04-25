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

import cn.asany.pim.product.convert.ProductCategoryConverter;
import cn.asany.pim.product.domain.ProductCategory;
import cn.asany.pim.product.graphql.input.ProductCategoryCreateInput;
import cn.asany.pim.product.graphql.input.ProductCategoryUpdateInput;
import cn.asany.pim.product.graphql.input.ProductCategoryWhereInput;
import cn.asany.pim.product.service.ProductCategoryService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class ProductCategoryGraphQLRootResolver
    implements GraphQLMutationResolver, GraphQLQueryResolver {

  private final ProductCategoryService productCategoryService;

  private final ProductCategoryConverter productCategoryConverter;

  public ProductCategoryGraphQLRootResolver(
      ProductCategoryService productCategoryService,
      ProductCategoryConverter productCategoryConverter) {
    this.productCategoryService = productCategoryService;
    this.productCategoryConverter = productCategoryConverter;
  }

  public List<ProductCategory> productCategories(
      ProductCategoryWhereInput where, int offset, int limit, Sort sort) {
    PropertyFilter filter =
        ObjectUtil.defaultValue(where, ProductCategoryWhereInput::new).toFilter();
    return productCategoryService.findAll(filter, offset, limit, sort);
  }

  public Optional<ProductCategory> productCategory(Long id) {
    return productCategoryService.findById(id);
  }

  public ProductCategory createProductCategory(ProductCategoryCreateInput input) {
    ProductCategory productCategory = productCategoryConverter.toProductCategory(input);
    return this.productCategoryService.save(productCategory);
  }

  public ProductCategory updateProductCategory(
      Long id, ProductCategoryUpdateInput input, Boolean merge) {
    ProductCategory productCategory = productCategoryConverter.toProductCategory(input);
    return this.productCategoryService.update(id, productCategory, merge);
  }

  public Optional<ProductCategory> deleteProductCategory(Long id) {
    return this.productCategoryService.delete(id);
  }
}
