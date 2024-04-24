package cn.asany.ui.library.dao.impl;

import cn.asany.ui.library.dao.LibraryItemDao;
import cn.asany.ui.library.domain.LibraryItem;
import jakarta.persistence.EntityManager;
import java.util.List;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.dao.jpa.SimpleAnyJpaRepository;

/**
 * LibraryItemDaoImpl
 *
 * @author limaofeng
 */
public class LibraryItemDaoImpl extends SimpleAnyJpaRepository<LibraryItem, Long>
    implements LibraryItemDao {

  public LibraryItemDaoImpl(EntityManager entityManager) {
    super(LibraryItem.class, entityManager);
  }

  @Override
  public List<LibraryItem> findAllByTagWithIcon(Long libraryId, String tag) {
    PropertyFilter filter = PropertyFilter.newFilter();
    filter.or(
        PropertyFilter.newFilter().equal("tags", tag),
        PropertyFilter.newFilter().startsWith("tags", tag + "/"));
    return this.findAll(filter.equal("library.id", libraryId));
  }

  @Override
  public List<LibraryItem> findAllByTagWithIcon(PropertyFilter filter) {
    return super.findAll(filter);
  }
}
