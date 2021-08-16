package cn.asany.ui.library.service;

import cn.asany.ui.library.bean.Library;
import cn.asany.ui.library.bean.LibraryItem;
import cn.asany.ui.library.bean.enums.LibraryType;
import cn.asany.ui.library.bean.enums.Operation;
import cn.asany.ui.library.dao.LibraryDao;
import cn.asany.ui.library.dao.LibraryItemDao;
import cn.asany.ui.resources.dao.IconDao;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.springframework.stereotype.Service;

@Service
public class LibraryService {

  private final IconDao iconDao;
  private final LibraryDao libraryDao;
  private final LibraryItemDao libraryItemDao;
  private final OplogService oplogService;

  public LibraryService(
      OplogService oplogService,
      IconDao iconDao,
      LibraryDao libraryDao,
      LibraryItemDao libraryItemDao) {
    this.iconDao = iconDao;
    this.libraryDao = libraryDao;
    this.libraryItemDao = libraryItemDao;
    this.oplogService = oplogService;
  }

  public List<Library> libraries(PropertyFilterBuilder builder, LibraryType type, boolean with) {
    if (LibraryType.ICONS == type) {
      builder.equal("type", LibraryType.ICONS);
      return with
          ? this.libraryDao.findAllWithIcon(builder.build())
          : this.libraryDao.findAll(builder.build());
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
    Optional<Library> optional = this.libraryDao.findByIdWithIcon(id);
    if (!optional.isPresent()) {
      return;
    }
    Library library = optional.get();
    this.libraryItemDao.deleteAllByIdInBatch(
        library.getItems().stream().map(LibraryItem::getId).collect(Collectors.toList()));
    if (library.getType() == LibraryType.ICONS) {
      this.iconDao.deleteAllByIdInBatch(
          library.getItems().stream().map(LibraryItem::getResourceId).collect(Collectors.toList()));
    }
    List<Long> ids = new ArrayList<>();
    ids.add(id);
    this.libraryDao.deleteAllByIdInBatch(ids);
    this.oplogService.log(Operation.DELETE, library);
  }

  public Long getResourceTotal(Long id) {
    return this.libraryItemDao.count(PropertyFilter.builder().equal("library.id", id).build());
  }
}
