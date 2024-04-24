package cn.asany.nuwa.app.dao;

import cn.asany.nuwa.app.domain.ApplicationRoute;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRouteDao extends AnyJpaRepository<ApplicationRoute, Long> {

  @EntityGraph(
      value = "Graph.ApplicationRoute.FetchComponent",
      type = EntityGraph.EntityGraphType.FETCH)
  List<ApplicationRoute> findAllByApplicationWithComponent(Long applicationId);
}
