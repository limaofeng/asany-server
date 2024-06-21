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
package cn.asany.nuwa.app.dao;

import cn.asany.nuwa.app.domain.Application;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

/**
 * 应用存储库
 *
 * @author limaofeng
 */
@Repository
public interface ApplicationDao extends AnyJpaRepository<Application, Long> {

  /**
   * 获取应用详情
   *
   * @param clientId 客户端ID
   * @return 应用详情
   */
  @EntityGraph(value = "Graph.Application.FetchDetails", type = EntityGraph.EntityGraphType.FETCH)
  Optional<Application> findDetailsByClientId(String clientId);

  /**
   * 获取应用详情
   *
   * @param id 应用ID
   * @return 应用详情
   */
  @EntityGraph(value = "Graph.Application.FetchDetails", type = EntityGraph.EntityGraphType.FETCH)
  Optional<Application> findDetailsById(Long id);

  /**
   * 获取应用详情
   *
   * @param clientId 应用ID
   * @return 应用详情
   */
  @EntityGraph(value = "Graph.Application.FetchRoutes", type = EntityGraph.EntityGraphType.FETCH)
  Optional<Application> findOneWithRoutesByClientId(String clientId);

  /**
   * 获取应用详情
   *
   * @param clientId 应用ID
   * @return 应用详情
   */
  @EntityGraph(value = "Graph.Application.FetchMenus", type = EntityGraph.EntityGraphType.FETCH)
  Optional<Application> findOneWithMenusByClientId(String clientId);

  /**
   * 获取应用详情
   *
   * @param id ID
   * @return 应用详情
   */
  @EntityGraph(value = "Graph.Application.FetchRoutes", type = EntityGraph.EntityGraphType.FETCH)
  Optional<Application> findOneWithRoutesById(Long id);

  /**
   * 获取应用详情
   *
   * @param id ID
   * @return 应用详情
   */
  @EntityGraph(value = "Graph.Application.FetchMenus", type = EntityGraph.EntityGraphType.FETCH)
  Optional<Application> findOneWithMenusById(Long id);

  @EntityGraph(
      value = "Graph.Application.FetchClientDetails",
      type = EntityGraph.EntityGraphType.FETCH)
  Optional<Application> findOneWithClientDetails(PropertyFilter filter);
}
