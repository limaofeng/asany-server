package cn.asany.shanhai.gateway.dao;

import cn.asany.shanhai.gateway.bean.ModelGroup;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModelGroupDao extends JpaRepository<ModelGroup, Long> {

    /**
     * 同时返回 items 字段
     *
     * @return
     */
    @EntityGraph(value = "Graph.ModelGroup.FetchChildren", type = EntityGraph.EntityGraphType.FETCH)
    List<ModelGroup> findAllWithItems();

}
