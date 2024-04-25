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

import cn.asany.security.core.dao.RoleScopeTypeDao;
import cn.asany.security.core.domain.RoleScopeType;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleScopeTypeService {

  private final RoleScopeTypeDao roleScopeTypeDao;

  public RoleScopeTypeService(RoleScopeTypeDao roleScopeTypeDao) {
    this.roleScopeTypeDao = roleScopeTypeDao;
  }

  public Page<RoleScopeType> findPage(Pageable pageable, PropertyFilter filter) {
    return this.roleScopeTypeDao.findPage(pageable, filter);
  }

  public RoleScopeType get(String id) {
    return this.roleScopeTypeDao.getReferenceById(id);
  }

  public RoleScopeType save(RoleScopeType entity) {
    return roleScopeTypeDao.save(entity);
  }

  public List<RoleScopeType> findAll(PropertyFilter filter) {
    return roleScopeTypeDao.findAll(filter);
  }

  public RoleScopeType update(Long id, boolean merge, RoleScopeType entity) {
    entity.setId(id);
    return this.roleScopeTypeDao.update(entity, merge);
  }
}
