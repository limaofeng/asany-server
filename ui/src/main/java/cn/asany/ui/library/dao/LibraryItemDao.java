package cn.asany.ui.library.dao;

import cn.asany.ui.library.bean.LibraryItem;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibraryItemDao extends JpaRepository<LibraryItem, Long> {

    @EntityGraph(value = "Graph.LibraryItem.FetchIcon", type = EntityGraph.EntityGraphType.FETCH)
    List<LibraryItem> findAllByTagWithIcon(Long libraryId, String tag);

    @EntityGraph(value = "Graph.LibraryItem.FetchIcon", type = EntityGraph.EntityGraphType.FETCH)
    List<LibraryItem> findAllByTagWithIcon(List<PropertyFilter> filters);

}
