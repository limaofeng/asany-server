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

import cn.asany.nuwa.app.dao.ApplicationRouteDao;
import cn.asany.nuwa.app.domain.ApplicationRoute;
import jakarta.persistence.EntityManager;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;

public class ApplicationRouteDaoImpl extends SimpleAnyJpaRepository<ApplicationRoute, Long>
    implements ApplicationRouteDao {

  public ApplicationRouteDaoImpl(EntityManager entityManager) {
    super(ApplicationRoute.class, entityManager);
  }

  @Override
  public List<ApplicationRoute> findAllByApplicationWithComponent(Long applicationId) {
    return this.findAll(PropertyFilter.newFilter().equal("application.id", applicationId));
  }
}
