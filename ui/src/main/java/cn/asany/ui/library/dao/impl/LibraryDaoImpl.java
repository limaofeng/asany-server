package cn.asany.ui.library.dao.impl;

import cn.asany.ui.library.bean.Library;
import cn.asany.ui.library.bean.enums.LibraryType;
import cn.asany.ui.library.dao.LibraryDao;
import org.jfantasy.framework.dao.jpa.ComplexJpaRepository;
import org.jfantasy.framework.dao.jpa.PropertyFilter;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

public class LibraryDaoImpl extends ComplexJpaRepository<Library, Long> implements LibraryDao {

    public LibraryDaoImpl(EntityManager entityManager) {
        super(Library.class, entityManager);
    }

    @Override
    public Optional<Library> findByIdWithIcon(Long id) {
        return this.findById(id);
    }

    @Override
    public List<Library> findAllWithIcon() {
        return this.findAll(PropertyFilter.builder().equal("type", LibraryType.ICONS).build());
    }

}
