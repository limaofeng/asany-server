package cn.asany.nuwa.app.dao;

import cn.asany.nuwa.app.domain.Routespace;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

/**
 * 路由空间
 *
 * @author limaofeng
 */
public interface RoutespaceDao extends JpaRepository<Routespace, String> {

  /**
   * 查询 Routespace 同时返回路由及模版信息
   *
   * @param ids Set<String>
   * @return List<Routespace>
   */
  @EntityGraph(
      value = "Graph.Routespace.FetchApplicationTemplate",
      type = EntityGraph.EntityGraphType.FETCH)
  List<Routespace> findByIdsWithApplicationTemplate(Set<String> ids);

  /**
   * 查询 Routespace 同时返回路由及模版信息
   *
   * @param id ID
   * @return Optional<Routespace>
   */
  @EntityGraph(
      value = "Graph.Routespace.FetchApplicationTemplate",
      type = EntityGraph.EntityGraphType.FETCH)
  Optional<Routespace> findByIdWithApplicationTemplate(String id);
}
