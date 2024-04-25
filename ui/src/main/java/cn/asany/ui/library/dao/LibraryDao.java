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
package cn.asany.ui.library.dao;

import cn.asany.ui.library.domain.Library;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryDao extends AnyJpaRepository<Library, Long> {

  @EntityGraph(value = "Graph.Library.FetchIcon", type = EntityGraph.EntityGraphType.FETCH)
  Optional<Library> findByIdWithIcon(Long id);

  @EntityGraph(value = "Graph.Library.FetchComponent", type = EntityGraph.EntityGraphType.FETCH)
  Optional<Library> findByIdWithComponent(Long id);

  @EntityGraph(value = "Graph.Library.FetchIcon", type = EntityGraph.EntityGraphType.FETCH)
  List<Library> findAllWithIcon(PropertyFilter filter);
}
