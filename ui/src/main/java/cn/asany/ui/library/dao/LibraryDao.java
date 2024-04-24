package cn.asany.ui.library.dao;

import cn.asany.ui.library.domain.Library;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

@Repository
public interface LibraryDao extends AnyJpaRepository<Library, Long> {

  @EntityGraph(value = "Graph.Library.FetchIcon", type = EntityGraph.EntityGraphType.FETCH)
  Optional<Library> findByIdWithIcon(Long id);

  @EntityGraph(value = "Graph.Library.FetchComponent", type = EntityGraph.EntityGraphType.FETCH)
  Optional<Library> findByIdWithComponent(Long id);

  @EntityGraph(value = "Graph.Library.FetchIcon", type = EntityGraph.EntityGraphType.FETCH)
  List<Library> findAllWithIcon(PropertyFilter filter);
}
