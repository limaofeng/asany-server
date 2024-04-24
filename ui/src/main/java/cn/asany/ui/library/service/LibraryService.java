package cn.asany.ui.library.service;

import cn.asany.ui.library.dao.LibraryDao;
import cn.asany.ui.library.dao.LibraryItemDao;
import cn.asany.ui.library.domain.Library;
import cn.asany.ui.library.domain.LibraryItem;
import cn.asany.ui.library.domain.enums.LibraryType;
import cn.asany.ui.library.domain.enums.Operation;
import cn.asany.ui.resources.dao.IconDao;
import cn.asany.ui.resources.domain.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 库 服务
 *
 * @author limaofeng
 */
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

  public List<Library> libraries(PropertyFilter filter, LibraryType type, boolean with) {
    if (LibraryType.ICONS == type) {
      filter.equal("type", LibraryType.ICONS);
      return with ? this.libraryDao.findAllWithIcon(filter) : this.libraryDao.findAll(filter);
    }
    return new ArrayList<>();
  }

  public Optional<Library> findById(Long id) {
    return this.libraryDao.findById(id);
  }

  public Optional<Library> findByIdWithComponent(Long id) {
    return this.libraryDao.findByIdWithComponent(id);
  }

  public Optional<Library> findByIdWithIcon(Long id) {
    return this.libraryDao.findByIdWithIcon(id);
  }

  public Library save(Library library) {
    return this.libraryDao.save(library);
  }

  public Library update(Long id, Library library) {
    Library oldLibrary = this.libraryDao.getReferenceById(id);
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
    return this.libraryItemDao.count(PropertyFilter.newFilter().equal("library.id", id));
  }

  @Transactional
  public void addComponent(Long libraryId, Component component, String... tags) {
    LibraryItem item =
        LibraryItem.builder()
            .library(Library.builder().id(libraryId).build())
            .resourceType("COMPONENT")
            .resourceId(component.getId())
            .tags(Arrays.asList(tags))
            .build();
    this.libraryItemDao.save(item);
  }
}
