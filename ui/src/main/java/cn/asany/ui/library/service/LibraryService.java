package cn.asany.ui.library.service;

import cn.asany.ui.library.bean.Library;
import cn.asany.ui.library.bean.enums.LibraryType;
import cn.asany.ui.library.dao.LibraryDao;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {

    private final LibraryDao libraryDao;

    public LibraryService(LibraryDao libraryDao) {
        this.libraryDao = libraryDao;
    }

    public List<Library> libraries(PropertyFilterBuilder builder, LibraryType type, boolean with) {
        if (LibraryType.ICONS == type) {
            builder.equal("type", LibraryType.ICONS);
            return with ? this.libraryDao.findAllWithIcon(builder.build()) : this.libraryDao.findAll(builder.build());
        }
        return new ArrayList<>();
    }

    public Optional<Library> findById(Long id) {
        return this.libraryDao.findById(id);
    }

    public Optional<Library> findByIdWithIcon(Long id) {
        return this.libraryDao.findByIdWithIcon(id);
    }

    public Library save(Library library) {
        return this.libraryDao.save(library);
    }

    public Library update(Long id, Library library) {
        Library oldLibrary = this.libraryDao.getById(id);
        oldLibrary.setName(library.getName());
        oldLibrary.setDescription(library.getDescription());
        return this.libraryDao.update(oldLibrary);
    }

    public void delete(Long id) {
        this.libraryDao.deleteById(id);
    }
}
