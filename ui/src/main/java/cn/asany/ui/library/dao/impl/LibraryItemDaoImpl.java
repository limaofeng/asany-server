package cn.asany.ui.library.dao.impl;

import cn.asany.ui.library.bean.LibraryItem;
import cn.asany.ui.library.dao.LibraryItemDao;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;

import javax.persistence.EntityManager;
import java.util.List;

public class LibraryItemDaoImpl extends ComplexJpaRepository<LibraryItem, Long> implements LibraryItemDao {

    public LibraryItemDaoImpl(EntityManager entityManager) {
        super(LibraryItem.class, entityManager);
    }

    @Override
    public List<LibraryItem> findAllByTagWithIcon(Long libraryId, String tag) {
        PropertyFilterBuilder builder = PropertyFilter.builder();
        builder.or(PropertyFilter.builder().equal("tags", tag).build(), PropertyFilter.builder().startsWith("tags", tag + "/").build());
        return this.findAll(builder.equal("library.id", libraryId).build());
    }

}
