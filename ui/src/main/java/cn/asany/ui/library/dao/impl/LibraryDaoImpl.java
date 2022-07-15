package cn.asany.ui.library.dao.impl;

import cn.asany.ui.library.dao.LibraryDao;
import cn.asany.ui.library.domain.Library;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.jfantasy.framework.dao.jpa.PropertyFilter;

public class LibraryDaoImpl extends ComplexJpaRepository<Library, Long> implements LibraryDao {

  public LibraryDaoImpl(EntityManager entityManager) {
    super(Library.class, entityManager);
  }

  @Override
  public Optional<Library> findByIdWithIcon(Long id) {
    return this.findById(id);
  }

  @Override
  public Optional<Library> findByIdWithComponent(Long id) {
    return this.findById(id);
  }

  @Override
  public List<Library> findAllWithIcon(List<PropertyFilter> filters) {
    return this.findAll(filters);
  }
}
