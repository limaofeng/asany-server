package cn.asany.ui.resources.service;

import cn.asany.ui.library.convert.LibraryConverter;
import cn.asany.ui.library.dao.LibraryDao;
import cn.asany.ui.library.dao.LibraryItemDao;
import cn.asany.ui.library.domain.Library;
import cn.asany.ui.library.domain.LibraryItem;
import cn.asany.ui.library.domain.enums.LibraryType;
import cn.asany.ui.resources.dao.IconDao;
import cn.asany.ui.resources.domain.Icon;
import cn.asany.ui.resources.domain.enums.IconType;
import java.util.*;
import java.util.stream.Collectors;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.stereotype.Service;

@Service
public class IconService {

  private final IconDao iconDao;
  private final LibraryDao libraryDao;
  private final LibraryItemDao libraryItemDao;
  private final LibraryConverter libraryConverter;

  public IconService(
      IconDao iconDao,
      LibraryDao libraryDao,
      LibraryItemDao libraryItemDao,
      LibraryConverter libraryConverter) {
    this.iconDao = iconDao;
    this.libraryDao = libraryDao;
    this.libraryItemDao = libraryItemDao;
    this.libraryConverter = libraryConverter;
  }

  public Set<Icon> findAllByTag(Long libraryId, String tag) {
    return this.libraryConverter.toIcons(
        new HashSet<>(this.libraryItemDao.findAllByTagWithIcon(libraryId, tag)));
  }

  public void delete(Long id) {
    Optional<Icon> optionalIcon = this.iconDao.findById(id);
    Optional<LibraryItem> optionalItem =
        this.libraryItemDao.findOne(
            PropertyFilter.builder()
                .equal("resourceId", id)
                .equal("resourceType", Icon.RESOURCE_NAME)
                .build());
    if (!optionalIcon.isPresent() || !optionalItem.isPresent()) {
      throw new ValidationException("图标{" + id + "}不存在");
    }
    this.libraryItemDao.delete(optionalItem.get());
    this.iconDao.delete(optionalIcon.get());
  }

  public Set<Icon> importIcons(Long libraryId, List<Icon> icons) {
    long start = System.currentTimeMillis();

    Optional<Library> optional = this.libraryDao.findByIdWithIcon(libraryId);
    if (!optional.isPresent()) {
      throw new ValidationException("图标库{" + libraryId + "}不正确");
    }
    Library library = optional.get();
    if (library.getType() != LibraryType.ICONS) {
      throw new ValidationException("图标库{" + libraryId + "}不正确");
    }

    List<LibraryItem> existed = new ArrayList<>(library.getItems());
    List<LibraryItem> itemSaveEntities = new ArrayList<>();
    List<LibraryItem> itemUpdateEntities = new ArrayList<>();
    List<Icon> updateEntities = new ArrayList<>();
    List<Icon> saveEntities = new ArrayList<>();

    List<LibraryItem> returnVal = new ArrayList<>();
    for (Icon icon : icons) {
      String key = StringUtil.isNotBlank(icon.getUnicode()) ? "unicode" : "name";
      String value = StringUtil.isNotBlank(icon.getUnicode()) ? icon.getUnicode() : icon.getName();
      LibraryItem item = ObjectUtil.find(existed, "resource." + key, value);
      if (item != null) {
        boolean modified = false;
        Icon oldIcon = item.getResource(Icon.class);
        if (!oldIcon.getContent().equals(icon.getContent())) {
          modified = true;
          oldIcon.setContent(icon.getContent());
        }
        if (oldIcon.get(Icon.METADATA_LIBRARY_ID) == null) {
          oldIcon.set(Icon.METADATA_LIBRARY_ID, libraryId.toString());
        }
        if (modified) {
          updateEntities.add(oldIcon);
        }
        if (icon.getTags() != null
            && !icon.getTags().isEmpty()
            && !Arrays.equals(icon.getTags().toArray(), item.getTags().toArray())) {
          item.setTags(icon.getTags());
          itemUpdateEntities.add(item);
        } else {
          returnVal.add(item);
        }
        continue;
      }
      icon.set(Icon.METADATA_LIBRARY_ID, libraryId.toString());
      icon.setType(IconType.SVG);
      saveEntities.add(icon);
      itemSaveEntities.add(
          LibraryItem.builder()
              .library(library)
              .tags(icon.getTags())
              .resourceType(Icon.RESOURCE_NAME)
              .resourceId(icon.getId())
              .resource(icon)
              .build());
    }

    this.iconDao.saveAllInBatch(saveEntities);
    this.iconDao.updateAllInBatch(updateEntities);
    this.libraryItemDao.saveAllInBatch(
        itemSaveEntities.stream()
            .peek(item -> item.setResourceId(item.getResource(Icon.class).getId()))
            .collect(Collectors.toList()));
    this.libraryItemDao.updateAllInBatch(itemUpdateEntities);

    long times = System.currentTimeMillis() - start;

    returnVal.addAll(itemSaveEntities);
    returnVal.addAll(itemUpdateEntities);
    System.out.println("处理图标:" + icons.size() + "个\t times = " + times);
    return this.libraryConverter.toIcons(returnVal.stream().collect(Collectors.toSet()));
  }

  public Icon save(Long libraryId, Icon icon) {
    icon.setType(IconType.SVG);

    Optional<Library> optional = this.libraryDao.findByIdWithIcon(libraryId);
    if (!optional.isPresent()) {
      throw new ValidationException("图标库{" + libraryId + "}不存在");
    }
    Library library = optional.get();
    if (library.getType() != LibraryType.ICONS) {
      throw new ValidationException("库{" + libraryId + "}, 类型不正确");
    }

    List<LibraryItem> existed = new ArrayList<>(library.getItems());
    LibraryItem item;
    if (icon.getId() != null) {
      item = ObjectUtil.find(existed, "resource.id", icon.getId());
    } else {
      item = ObjectUtil.find(existed, "resource.unicode", icon.getUnicode());
    }

    if (item != null) {
      Icon oldIcon = item.getResource(Icon.class);
      if (icon.getName() != null) {
        oldIcon.setName(icon.getName());
      }
      if (icon.getTags() != null) {
        oldIcon.setTags(icon.getTags());
      }
      if (icon.getDescription() != null) {
        oldIcon.setDescription(icon.getDescription());
      }
      if (icon.getUnicode() != null) {
        oldIcon.setUnicode(icon.getUnicode());
      }
      if (icon.getContent() != null) {
        oldIcon.setContent(icon.getContent());
      }
      oldIcon.set(Icon.METADATA_LIBRARY_ID, libraryId.toString());
      this.iconDao.update(oldIcon);
      if (icon.getTags() != null
          && !icon.getTags().isEmpty()
          && !Arrays.equals(icon.getTags().toArray(), item.getTags().toArray())) {
        item.setTags(icon.getTags());
        this.libraryItemDao.update(item);
      }
      return oldIcon;
    }
    icon.set(Icon.METADATA_LIBRARY_ID, libraryId.toString());
    this.iconDao.save(icon);
    item =
        LibraryItem.builder()
            .library(library)
            .tags(icon.getTags())
            .resourceType(Icon.RESOURCE_NAME)
            .resourceId(icon.getId())
            .resource(icon)
            .build();
    this.libraryItemDao.save(item);
    return icon;
  }

  public Set<Icon> findAll(List<PropertyFilter> filters) {
    return libraryConverter.toIcons(
        this.libraryItemDao.findAllByTagWithIcon(filters).stream().collect(Collectors.toSet()));
  }
}
