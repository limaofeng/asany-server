package cn.asany.ui.library.dao;

import cn.asany.ui.library.domain.LibraryItem;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryItemDao extends AnyJpaRepository<LibraryItem, Long> {

  @EntityGraph(value = "Graph.LibraryItem.FetchIcon", type = EntityGraph.EntityGraphType.FETCH)
  List<LibraryItem> findAllByTagWithIcon(Long libraryId, String tag);

  @EntityGraph(value = "Graph.LibraryItem.FetchIcon", type = EntityGraph.EntityGraphType.FETCH)
  List<LibraryItem> findAllByTagWithIcon(PropertyFilter filter);
}
