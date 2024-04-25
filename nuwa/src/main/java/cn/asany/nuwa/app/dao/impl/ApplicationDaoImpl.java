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
package cn.asany.nuwa.app.dao.impl;

import cn.asany.nuwa.app.dao.ApplicationDao;
import cn.asany.nuwa.app.domain.Application;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;

/**
 * 应用 数据存储类
 *
 * @author limaofeng
 */
public class ApplicationDaoImpl extends SimpleAnyJpaRepository<Application, Long>
    implements ApplicationDao {

  public ApplicationDaoImpl(EntityManager entityManager) {
    super(Application.class, entityManager);
  }

  @Override
  public Optional<Application> findDetailsByClientId(String clientId) {
    return this.findOne(PropertyFilter.newFilter().equal("clientId", clientId));
  }

  @Override
  public Optional<Application> findDetailsById(Long id) {
    return this.findOne(PropertyFilter.newFilter().equal("id", id));
  }

  @Override
  public Optional<Application> findOneWithRoutesByClientId(String clientId) {
    return this.findOne(PropertyFilter.newFilter().equal("clientId", clientId));
  }

  @Override
  public Optional<Application> findOneWithMenusByClientId(String clientId) {
    return this.findOne(PropertyFilter.newFilter().equal("clientId", clientId));
  }

  @Override
  public Optional<Application> findOneWithRoutesById(Long id) {
    return this.findOne(PropertyFilter.newFilter().equal("id", id));
  }

  @Override
  public Optional<Application> findOneWithMenusById(Long id) {
    return this.findOne(PropertyFilter.newFilter().equal("id", id));
  }

  @Override
  public Optional<Application> findOneWithClientDetails(PropertyFilter filter) {
    return this.findOne(filter);
  }
}
