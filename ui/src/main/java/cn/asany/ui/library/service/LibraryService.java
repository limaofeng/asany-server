package cn.asany.ui.library.service;

import cn.asany.ui.library.bean.Library;
import cn.asany.ui.library.bean.enums.LibraryType;
import cn.asany.ui.library.dao.LibraryDao;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {

    private final LibraryDao libraryDao;

    public LibraryService(LibraryDao libraryDao) {
        this.libraryDao = libraryDao;
    }

    public List<Library> libraries(LibraryType type) {
        return this.libraryDao.findAll(PropertyFilter.builder().equal("type", type).build());
    }

    public Optional<Library> get(Long id) {
        return this.libraryDao.findById(id);
    }

}
