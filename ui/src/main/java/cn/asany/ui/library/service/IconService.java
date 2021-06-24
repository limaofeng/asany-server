package cn.asany.ui.library.service;

import cn.asany.ui.library.bean.Library;
import cn.asany.ui.library.bean.LibraryItem;
import cn.asany.ui.library.dao.LibraryItemDao;
import cn.asany.ui.resources.bean.Icon;
import cn.asany.ui.resources.bean.enums.IconType;
import cn.asany.ui.resources.dao.IconDao;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class IconService {

    private final IconDao iconDao;
    private final LibraryItemDao libraryItemDao;

    public IconService(IconDao iconDao, LibraryItemDao libraryItemDao) {
        this.iconDao = iconDao;
        this.libraryItemDao = libraryItemDao;
    }

    @Transactional()
    public void importIcons(Long libraryId, List<Icon> icons) {
        Library library = Library.builder().id(libraryId).build();

        Long x = System.currentTimeMillis();

        icons = icons.subList(0, 40);

        List<LibraryItem> items = libraryItemDao.findAll(PropertyFilter.builder().equal("library.id", libraryId).build());

        for (Icon icon : icons) {
            LibraryItem item = ObjectUtil.find(items, "resource.name", icon.getName());
            if (item != null) {
                icon.setId(item.getId());
            }
            icon.setType(IconType.SVG);
            this.iconDao.save(icon);
            this.libraryItemDao.save(LibraryItem.builder().library(library).resourceType(Icon.RESOURCE_NAME).resourceId(icon.getId()).resource(icon).build());
        }

        long times = System.currentTimeMillis() - x;

        System.out.println("保存成功" + icons.size() + " times = " + times);

    }
}
