package cn.asany.ui.library.service;

import cn.asany.ui.library.bean.Library;
import cn.asany.ui.library.bean.LibraryItem;
import cn.asany.ui.library.bean.enums.LibraryType;
import cn.asany.ui.library.converter.LibraryConverter;
import cn.asany.ui.library.dao.LibraryDao;
import cn.asany.ui.library.dao.LibraryItemDao;
import cn.asany.ui.resources.bean.Icon;
import cn.asany.ui.resources.bean.enums.IconType;
import cn.asany.ui.resources.dao.IconDao;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public void delete(Long libraryId) {
        Optional<Library> optional = this.libraryDao.findByIdWithIcon(libraryId);
        if (!optional.isPresent()) {
            return;
        }
        Library library = optional.get();
        if (library.getType() != LibraryType.ICONS) {
            return;
        }
        this.libraryItemDao.deleteById(libraryId);
        this.iconDao.deleteAllByIdInBatch(library.getItems().stream().map(item -> item.getResourceId()).collect(Collectors.toList()));
    }

    public void importIcons(Long libraryId, List<Icon> icons) {
        long start = System.currentTimeMillis();

        Optional<Library> optional = this.libraryDao.findByIdWithIcon(libraryId);
        if (!optional.isPresent()) {
            return;
        }
        Library library = optional.get();
        if (library.getType() != LibraryType.ICONS) {
            return;
        }

        List<LibraryItem> existed = new ArrayList<>(library.getItems());
        List<LibraryItem> itemSaveEntities = new ArrayList<>();
        List<LibraryItem> itemUpdateEntities = new ArrayList<>();
        List<Icon> updateEntities = new ArrayList<>();
        List<Icon> saveEntities = new ArrayList<>();

        for (Icon icon : icons) {
            LibraryItem item = ObjectUtil.find(existed, "resource.unicode", icon.getUnicode());
            if (item != null) {
                Icon oldIcon = item.getResource(Icon.class);
                oldIcon.setContent(icon.getContent());
                updateEntities.add(oldIcon);
                if (!icon.getTags().isEmpty()) {
                    item.setTags(icon.getTags());
                    itemUpdateEntities.add(item);
                }
                continue;
            }
            icon.setType(IconType.SVG);
            saveEntities.add(icon);
            itemSaveEntities.add(LibraryItem.builder().library(library).tags(icon.getTags()).resourceType(Icon.RESOURCE_NAME).resourceId(icon.getId()).resource(icon).build());
        }

        this.iconDao.saveAllInBatch(saveEntities);
        this.iconDao.updateAllInBatch(updateEntities);
        this.libraryItemDao.saveAllInBatch(itemSaveEntities.stream().peek(item -> item.setResourceId(item.getResource(Icon.class).getId())).collect(Collectors.toList()));
        this.libraryItemDao.updateAllInBatch(itemUpdateEntities);

        long times = System.currentTimeMillis() - start;

        System.out.println("保存成功" + icons.size() + " times = " + times);
    }

}
