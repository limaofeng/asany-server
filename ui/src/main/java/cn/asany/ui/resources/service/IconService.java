package cn.asany.ui.resources.service;

import cn.asany.ui.library.bean.Library;
import cn.asany.ui.library.bean.LibraryItem;
import cn.asany.ui.library.bean.enums.LibraryType;
import cn.asany.ui.library.converter.LibraryConverter;
import cn.asany.ui.library.dao.LibraryDao;
import cn.asany.ui.library.dao.LibraryItemDao;
import cn.asany.ui.resources.bean.Icon;
import cn.asany.ui.resources.bean.enums.IconType;
import cn.asany.ui.resources.dao.IconDao;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.error.ValidationException;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IconService {

    private final IconDao iconDao;
    private final LibraryDao libraryDao;
    private final LibraryItemDao libraryItemDao;
    private final LibraryConverter libraryConverter;

    public IconService(IconDao iconDao, LibraryDao libraryDao, LibraryItemDao libraryItemDao, LibraryConverter libraryConverter) {
        this.iconDao = iconDao;
        this.libraryDao = libraryDao;
        this.libraryItemDao = libraryItemDao;
        this.libraryConverter = libraryConverter;
    }

    public List<Icon> findAllByTag(Long libraryId, String tag) {
        return this.libraryConverter.toIcons(this.libraryItemDao.findAllByTagWithIcon(libraryId, tag));
    }

    public void deleteLibrary(Long libraryId) {
        Optional<Library> optional = this.libraryDao.findByIdWithIcon(libraryId);
        if (!optional.isPresent()) {
            return;
        }
        Library library = optional.get();
        if (library.getType() != LibraryType.ICONS) {
            return;
        }
        this.libraryItemDao.deleteById(libraryId);
        this.iconDao.deleteAllByIdInBatch(library.getItems().stream().map(LibraryItem::getResourceId).collect(Collectors.toList()));
    }

    public void delete(Long id) {
        Optional<Icon> optionalIcon = this.iconDao.findById(id);
        Optional<LibraryItem> optionalItem = this.libraryItemDao.findOne(PropertyFilter.builder().equal("resourceId", id).equal("resourceType", Icon.RESOURCE_NAME).build());
        if (!optionalIcon.isPresent() || !optionalItem.isPresent()) {
            throw new ValidationException("图标{" + id + "}不存在");
        }
        this.libraryItemDao.delete(optionalItem.get());
        this.iconDao.delete(optionalIcon.get());

    }

    public List<Icon> importIcons(Long libraryId, List<Icon> icons) {
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
            LibraryItem item = ObjectUtil.find(existed, "resource.unicode", icon.getUnicode());
            if (item != null) {
                Icon oldIcon = item.getResource(Icon.class);
                oldIcon.setContent(icon.getContent());
                oldIcon.set(Icon.METADATA_LIBRARY_ID, libraryId.toString());
                updateEntities.add(oldIcon);
                if (icon.getTags() != null && !icon.getTags().isEmpty() && !Arrays.equals(icon.getTags().toArray(), item.getTags().toArray())) {
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
            itemSaveEntities.add(LibraryItem.builder().library(library).tags(icon.getTags()).resourceType(Icon.RESOURCE_NAME).resourceId(icon.getId()).resource(icon).build());
        }

        this.iconDao.saveAllInBatch(saveEntities);
        this.iconDao.updateAllInBatch(updateEntities);
        this.libraryItemDao.saveAllInBatch(itemSaveEntities.stream().peek(item -> item.setResourceId(item.getResource(Icon.class).getId())).collect(Collectors.toList()));
        this.libraryItemDao.updateAllInBatch(itemUpdateEntities);

        long times = System.currentTimeMillis() - start;

        returnVal.addAll(itemSaveEntities);
        returnVal.addAll(itemUpdateEntities);
        System.out.println("保存成功:" + icons.size() + "\t times = " + times);
        return this.libraryConverter.toIcons(returnVal);
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
            if (icon.getTags() != null && !icon.getTags().isEmpty() && !Arrays.equals(icon.getTags().toArray(), item.getTags().toArray())) {
                item.setTags(icon.getTags());
                this.libraryItemDao.update(item);
            }
            return oldIcon;
        }
        icon.set(Icon.METADATA_LIBRARY_ID, libraryId.toString());
        this.iconDao.save(icon);
        item = LibraryItem.builder().library(library).tags(icon.getTags()).resourceType(Icon.RESOURCE_NAME).resourceId(icon.getId()).resource(icon).build();
        this.libraryItemDao.save(item);
        return icon;
    }

    public List<Icon> findAll(List<PropertyFilter> filters) {
        return libraryConverter.toIcons(this.libraryItemDao.findAllByTagWithIcon(filters));
    }
}
