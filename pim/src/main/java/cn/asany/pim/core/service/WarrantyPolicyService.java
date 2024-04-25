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
package cn.asany.pim.core.service;

import cn.asany.pim.core.dao.WarrantyPolicyDao;
import cn.asany.pim.core.domain.WarrantyPolicy;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.stereotype.Service;

@Service
public class WarrantyPolicyService {

  private final WarrantyPolicyDao warrantyPolicyDao;

  public WarrantyPolicyService(WarrantyPolicyDao warrantyPolicyDao) {
    this.warrantyPolicyDao = warrantyPolicyDao;
  }

  public List<WarrantyPolicy> findAll(PropertyFilter filter) {
    return this.warrantyPolicyDao.findAll(filter);
  }

  public Optional<WarrantyPolicy> findById(Long id) {
    return this.warrantyPolicyDao.findById(id);
  }

  public WarrantyPolicy save(WarrantyPolicy warrantyPolicy) {
    return this.warrantyPolicyDao.save(warrantyPolicy);
  }

  public Optional<WarrantyPolicy> update(Long id, WarrantyPolicy warrantyPolicy, Boolean merge) {
    return this.warrantyPolicyDao
        .findById(id)
        .map(
            (result) -> {
              warrantyPolicy.setId(result.getId());
              return this.warrantyPolicyDao.update(warrantyPolicy, merge);
            });
  }

  public Integer deleteMany(PropertyFilter filter) {
    List<WarrantyPolicy> policies = this.warrantyPolicyDao.findAll(filter);
    this.warrantyPolicyDao.deleteAll(policies);
    return policies.size();
  }

  public Optional<WarrantyPolicy> delete(Long id) {
    return this.warrantyPolicyDao
        .findById(id)
        .map(
            (result) -> {
              this.warrantyPolicyDao.delete(result);
              return result;
            });
  }
}
