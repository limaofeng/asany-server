package cn.asany.shanhai.core.dao;

import cn.asany.shanhai.core.domain.ModelField;
import java.util.List;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/** @author limaofeng */
@Repository
public interface ModelFieldDao extends JpaRepository<ModelField, Long> {

  /**
   * 同时返回 Model 与 Type 对象
   *
   * @param filters
   * @return
   */
  @EntityGraph(
      value = "Graph.ModelField.FetchModelAndType",
      type = EntityGraph.EntityGraphType.FETCH)
  List<ModelField> findWithModelAndType(PropertyFilter filter);

  @EntityGraph(
      value = "Graph.ModelField.FetchModelAndType",
      type = EntityGraph.EntityGraphType.FETCH)
  @Query(
      "FROM ModelField mf "
          + "LEFT JOIN Model m ON m.id = mf.model.id "
          + "LEFT JOIN ModelGroupItem gi ON gi.resourceId = mf.id AND gi.resourceType = 'ENDPOINT' "
          + "WHERE m.code in ( 'Query', 'Mutation' ) AND gi.id is null")
  List<ModelField> findByUngrouped();
}
