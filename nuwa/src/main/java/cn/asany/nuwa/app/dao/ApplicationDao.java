package cn.asany.nuwa.app.dao;

import cn.asany.nuwa.app.bean.Application;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

/**
 * 应用存储库
 *
 * @author limaofeng
 */
@Repository
public interface ApplicationDao extends JpaRepository<Application, Long> {

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
}
