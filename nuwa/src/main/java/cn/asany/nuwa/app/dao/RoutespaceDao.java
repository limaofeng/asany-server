package cn.asany.nuwa.app.dao;

import cn.asany.nuwa.app.bean.Routespace;
import java.util.List;
import java.util.Set;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;

public interface RoutespaceDao extends JpaRepository<Routespace, String> {

  @EntityGraph(
      value = "Graph.Routespace.FetchApplicationTemplateRoute",
      type = EntityGraph.EntityGraphType.FETCH)
  List<Routespace> findByIdsWithApplicationTemplateRoute(Set<String> ids);
}
