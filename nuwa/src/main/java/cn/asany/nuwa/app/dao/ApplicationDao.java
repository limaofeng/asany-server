package cn.asany.nuwa.app.dao;

import cn.asany.nuwa.app.bean.Application;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationDao extends JpaRepository<Application, Long> {

    @EntityGraph(value = "Graph.Application.FetchRoute", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Application> findByClientIdWithRoute(String clientId, String space);

    @EntityGraph(value = "Graph.Application.FetchRoute", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Application> findByIdWithRoute(Long id, String space);

}
