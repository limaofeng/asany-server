package cn.asany.website.landing.dao;

import cn.asany.website.landing.domain.LandingPage;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

@Repository
public interface LandingPageDao extends JpaRepository<LandingPage, Long> {

  @EntityGraph(value = "Graph.LandingPage.FetchDetails", type = EntityGraph.EntityGraphType.FETCH)
  Optional<LandingPage> findByIdWithPosterAndStores(Long id);
}
