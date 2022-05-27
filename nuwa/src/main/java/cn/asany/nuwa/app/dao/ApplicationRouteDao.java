package cn.asany.nuwa.app.dao;

import cn.asany.nuwa.app.domain.ApplicationRoute;
import java.util.List;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

public interface ApplicationRouteDao extends JpaRepository<ApplicationRoute, Long> {

  @EntityGraph(
      value = "Graph.ApplicationRoute.FetchComponent",
      type = EntityGraph.EntityGraphType.FETCH)
  List<ApplicationRoute> findAllByApplicationAndSpaceWithComponent(
      Long applicationId, String space);
}
