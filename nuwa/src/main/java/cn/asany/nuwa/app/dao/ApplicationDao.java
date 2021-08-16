package cn.asany.nuwa.app.dao;

import cn.asany.nuwa.app.bean.Application;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationDao extends JpaRepository<Application, Long> {

  @EntityGraph(value = "Graph.Application.FetchRoute", type = EntityGraph.EntityGraphType.FETCH)
  Optional<Application> findByClientIdWithRoute(String clientId);

  @EntityGraph(value = "Graph.Application.FetchRoute", type = EntityGraph.EntityGraphType.FETCH)
  Optional<Application> findByIdWithRoute(Long id);
}
