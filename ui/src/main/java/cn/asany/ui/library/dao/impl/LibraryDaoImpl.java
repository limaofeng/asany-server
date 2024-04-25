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
package cn.asany.ui.library.dao.impl;

import cn.asany.ui.library.dao.LibraryDao;
import cn.asany.ui.library.domain.Library;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;

public class LibraryDaoImpl extends SimpleAnyJpaRepository<Library, Long> implements LibraryDao {

  public LibraryDaoImpl(EntityManager entityManager) {
    super(Library.class, entityManager);
  }

  @Override
  public Optional<Library> findByIdWithIcon(Long id) {
    return this.findById(id);
  }

  @Override
  public Optional<Library> findByIdWithComponent(Long id) {
    return this.findById(id);
  }

  @Override
  public List<Library> findAllWithIcon(PropertyFilter filter) {
    return this.findAll(filter);
  }
}
