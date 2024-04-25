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

import cn.asany.pim.product.dao.BrandDao;
import cn.asany.pim.product.domain.Brand;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BrandService {

  private final BrandDao brandDao;

  public BrandService(BrandDao brandDao) {
    this.brandDao = brandDao;
  }

  public Page<Brand> findPage(Pageable pageable, PropertyFilter filter) {
    return this.brandDao.findPage(pageable, filter);
  }

  public List<Brand> findAll(PropertyFilter filter, int offset, int limit, Sort sort) {
    return this.brandDao.findAll(filter, offset, limit, sort);
  }

  public Optional<Brand> findById(String id) {
    return this.brandDao.findById(id);
  }

  public Brand save(Brand brand) {
    return this.brandDao.save(brand);
  }

  public Brand update(String id, Brand brand, Boolean merge) {
    brand.setId(id);
    return this.brandDao.update(brand, merge);
  }

  public Optional<Brand> delete(String id) {
    return this.brandDao
        .findById(id)
        .map(
            brand -> {
              this.brandDao.delete(brand);
              return brand;
            });
  }

  public Integer deleteMany(PropertyFilter filter) {
    List<Brand> brands = this.brandDao.findAll(filter);
    this.brandDao.deleteAll(brands);
    return brands.size();
  }
}
