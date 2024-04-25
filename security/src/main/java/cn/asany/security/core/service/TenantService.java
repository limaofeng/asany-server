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
package cn.asany.security.core.service;

import cn.asany.security.core.dao.AccessControlSettingsDao;
import cn.asany.security.core.dao.TenantDao;
import cn.asany.security.core.domain.AccessControlSettings;
import cn.asany.security.core.domain.Tenant;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TenantService {

  private final TenantDao tenantDao;
  private final AccessControlSettingsDao accessControlSettingsDao;

  public TenantService(AccessControlSettingsDao accessControlSettingsDao, TenantDao tenantDao) {
    this.tenantDao = tenantDao;
    this.accessControlSettingsDao = accessControlSettingsDao;
  }

  public Page<Tenant> findPage(Pageable pageable, PropertyFilter filter) {
    return this.tenantDao.findPage(pageable, filter);
  }

  public Tenant save(Tenant tenant) {
    return this.tenantDao.save(tenant);
  }

  public Optional<AccessControlSettings> findAccessControlSettings(String tenantId) {
    return this.accessControlSettingsDao.findOne(
        PropertyFilter.newFilter().equal("tenant.id", tenantId));
  }

  public Optional<Tenant> findById(String id) {
    return this.tenantDao.findById(id);
  }

  public Tenant update(String id, Tenant tenant, boolean merge) {
    tenant.setId(id);
    return this.tenantDao.update(tenant, merge);
  }

  public void delete(String id) {
    this.tenantDao.deleteById(id);
  }
}
