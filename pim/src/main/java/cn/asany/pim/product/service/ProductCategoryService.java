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
package cn.asany.pim.product.service;

import cn.asany.pim.product.dao.ProductCategoryDao;
import cn.asany.pim.product.domain.ProductCategory;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ProductCategoryService {

  private final ProductCategoryDao productCategoryDao;

  public ProductCategoryService(ProductCategoryDao productCategoryDao) {
    this.productCategoryDao = productCategoryDao;
  }

  public List<ProductCategory> findAll(PropertyFilter filter, int offset, int limit, Sort sort) {
    return this.productCategoryDao.findAll(filter, offset, limit, sort);
  }

  public Optional<ProductCategory> findById(Long id) {
    return this.productCategoryDao.findById(id);
  }

  public ProductCategory save(ProductCategory productCategory) {
    return this.productCategoryDao.save(productCategory);
  }

  public ProductCategory update(Long id, ProductCategory productCategory, Boolean merge) {
    productCategory.setId(id);
    return this.productCategoryDao.update(productCategory, merge);
  }

  public Optional<ProductCategory> delete(Long id) {
    Optional<ProductCategory> category = this.productCategoryDao.findById(id);
    category.ifPresent(this.productCategoryDao::delete);
    return category;
  }
}
