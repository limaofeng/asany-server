package cn.asany.shanhai.core.dao;

import cn.asany.shanhai.core.bean.Model;
import cn.asany.shanhai.core.bean.enums.ModelType;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelDao extends JpaRepository<Model, Long> {

    @Query("FROM Model m " +
        "LEFT JOIN ModelGroupItem gi ON gi.resourceId = m.id AND gi.resourceType = 'MODEL' " +
        "WHERE m.code not in ( 'Query', 'Mutation' ) AND gi.id is null")
    List<Model> findByUngrouped();

    @EntityGraph(value = "Graph.Model.FetchMetadataAndFields", type = EntityGraph.EntityGraphType.FETCH)
    List<Model> findAllByTypesWithMetadataAndFields(ModelType... types);


}
