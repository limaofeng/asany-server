package cn.asany.ui.library.dao;

import cn.asany.ui.library.bean.Library;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibraryDao extends JpaRepository<Library, Long> {

    @EntityGraph(value = "Graph.Library.FetchIcon", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Library> findByIdWithIcon(Long id);

    @EntityGraph(value = "Graph.Library.FetchIcon", type = EntityGraph.EntityGraphType.FETCH)
    List<Library> findAllWithIcon(List<PropertyFilter> filters);

}
