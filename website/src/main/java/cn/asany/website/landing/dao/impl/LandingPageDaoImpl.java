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
package cn.asany.website.landing.dao.impl;

import cn.asany.website.landing.dao.LandingPageDao;
import cn.asany.website.landing.domain.LandingPage;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;

public class LandingPageDaoImpl extends SimpleAnyJpaRepository<LandingPage, Long>
    implements LandingPageDao {
  public LandingPageDaoImpl(EntityManager entityManager) {
    super(LandingPage.class, entityManager);
  }

  @Override
  public Optional<LandingPage> findByIdWithPosterAndStores(Long id) {
    return this.findById(id);
  }
}
