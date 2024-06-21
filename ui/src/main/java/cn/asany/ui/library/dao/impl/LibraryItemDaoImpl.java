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

import cn.asany.ui.library.dao.LibraryItemDao;
import cn.asany.ui.library.domain.LibraryItem;
import jakarta.persistence.EntityManager;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;

/**
 * LibraryItemDaoImpl
 *
 * @author limaofeng
 */
public class LibraryItemDaoImpl extends SimpleAnyJpaRepository<LibraryItem, Long>
    implements LibraryItemDao {

  public LibraryItemDaoImpl(EntityManager entityManager) {
    super(LibraryItem.class, entityManager);
  }

  @Override
  public List<LibraryItem> findAllByTagWithIcon(Long libraryId, String tag) {
    PropertyFilter filter = PropertyFilter.newFilter();
    filter.or(
        PropertyFilter.newFilter().equal("tags", tag),
        PropertyFilter.newFilter().startsWith("tags", tag + "/"));
    return this.findAll(filter.equal("library.id", libraryId));
  }

  @Override
  public List<LibraryItem> findAllByTagWithIcon(PropertyFilter filter) {
    return super.findAll(filter);
  }
}
