package cn.asany.ui.library.dao.impl;

import cn.asany.ui.library.dao.LibraryDao;
import cn.asany.ui.library.domain.Library;
import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;

public class LibraryDaoImpl extends SimpleAnyJpaRepository<Library, Long> implements LibraryDao {

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
  public List<Library> findAllWithIcon(PropertyFilter filter) {
    return this.findAll(filter);
  }
}
