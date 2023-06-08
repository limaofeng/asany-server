package cn.asany.shanhai.core.dao;

import cn.asany.shanhai.core.domain.Model;
import java.util.List;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelDao extends JpaRepository<Model, Long> {

  @Query(
      "FROM Model m "
          + "LEFT JOIN ModelGroupItem gi ON gi.resourceId = m.id AND gi.resourceType = 'MODEL' "
          + "WHERE m.code not in ( 'Query', 'Mutation' ) AND gi.id is null")
  List<Model> findByUngrouped();

  @EntityGraph(
      value = "Graph.Model.FetchMetadataAndFields",
      type = EntityGraph.EntityGraphType.FETCH)
  Model getDetails(Long id);

  @EntityGraph(
      value = "Graph.Model.FetchMetadataAndFields",
      type = EntityGraph.EntityGraphType.FETCH)
  List<Model> findAllWithMetadataAndFields(PropertyFilter filter, Sort orderBy);
}
