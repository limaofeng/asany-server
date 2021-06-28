package cn.asany.ui.library.service;

import cn.asany.ui.library.bean.Library;
import cn.asany.ui.library.bean.enums.LibraryType;
import cn.asany.ui.library.dao.LibraryDao;
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

    public List<Library> libraries(LibraryType type) {
        if (LibraryType.ICONS == type) {
            return this.libraryDao.findAllWithIcon();
        }
        return new ArrayList<>();
    }

    public Optional<Library> findById(Long id) {
        return this.libraryDao.findById(id);
    }

    public Optional<Library> findByIdWithIcon(Long id) {
        return this.libraryDao.findByIdWithIcon(id);
    }

}
