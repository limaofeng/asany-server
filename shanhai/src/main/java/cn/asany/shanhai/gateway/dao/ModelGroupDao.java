package cn.asany.shanhai.gateway.dao;

import cn.asany.shanhai.gateway.domain.ModelGroup;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelGroupDao extends AnyJpaRepository<ModelGroup, Long> {

  /**
   * 同时返回 items 字段
   *
   * @return
   */
  @EntityGraph(value = "Graph.ModelGroup.FetchChildren", type = EntityGraph.EntityGraphType.FETCH)
  List<ModelGroup> findAllWithItems();
}
