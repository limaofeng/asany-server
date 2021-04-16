package cn.asany.shanhai.core.dao;

import cn.asany.shanhai.core.bean.ModelField;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author limaofeng
 */
@Repository
public interface ModelFieldDao extends JpaRepository<ModelField, Long> {

    /**
     * 同时返回 Model 与 Type 对象
     *
     * @param filters
     * @return
     */
    @EntityGraph(value = "Graph.ModelField.FetchModelAndType", type = EntityGraph.EntityGraphType.FETCH)
    List<ModelField> findWithModelAndType(List<PropertyFilter> filters);
}