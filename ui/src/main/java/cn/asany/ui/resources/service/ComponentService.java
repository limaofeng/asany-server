package cn.asany.ui.resources.service;

import cn.asany.ui.library.dao.LibraryItemDao;
import cn.asany.ui.library.domain.Library;
import cn.asany.ui.library.domain.LibraryItem;
import cn.asany.ui.resources.dao.ComponentDao;
import cn.asany.ui.resources.domain.Component;
import cn.asany.ui.resources.domain.enums.ComponentScope;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * 组件服务
 *
 * @author limaofeng
 */
@Service
public class ComponentService {

  public static final String CACHE_KEY = "COMPONENT";
  private final ComponentDao componentDao;
  private final LibraryItemDao libraryItemDao;

  public ComponentService(ComponentDao componentDao, LibraryItemDao libraryItemDao) {
    this.componentDao = componentDao;
    this.libraryItemDao = libraryItemDao;
  }

  public Page<Component> findPage(Pageable pageable, List<PropertyFilter> filters) {
    return componentDao.findPage(pageable, filters);
  }

  public Component save(Component component) {
    return this.save(component, null);
  }

  public Component save(Component component, Long libraryId) {
    if (component.getScope() == null) {
      component.setScope(ComponentScope.ROUTE);
    }
    if (component.getBlocks() == null) {
      component.setBlocks(new ArrayList<>());
    }
    List<String> tags = component.getTags();
    Component _component = componentDao.save(component);
    if (libraryId != null) {
      this.libraryItemDao.save(
          LibraryItem.builder()
              .resourceId(_component.getId())
              .resourceType("COMPONENT")
              .library(Library.builder().id(libraryId).build())
              .tags(tags)
              .build());
    }
    return _component;
  }

  public Component update(Long id, Component component, Long libraryId, Boolean merge) {
    component.setId(id);
    List<String> tags = component.getTags();
    Component _component = this.componentDao.update(component, merge);
    if (libraryId != null) {
      Optional<LibraryItem> itemOptional =
          this.libraryItemDao.findOne(
              PropertyFilter.builder()
                  .equal("library.id", libraryId)
                  .equal("resourceId", id)
                  .build());
      if (itemOptional.isPresent()) {
        LibraryItem item = itemOptional.get();
        item.setTags(tags);
        this.libraryItemDao.save(item);
        _component.setTags(item.getTags());
      }
    }
    return _component;
  }

  public void delete(Long id) {
    this.libraryItemDao.deleteAll(
        this.libraryItemDao.findAll(PropertyFilter.builder().equal("resourceId", id).build()));
    this.componentDao.deleteById(id);
  }

  @Cacheable(key = "targetClass  + '.' +  methodName + '#' + #p0", value = CACHE_KEY)
  public Optional<Component> findById(Long id) {
    return this.componentDao.findById(id);
  }
}
