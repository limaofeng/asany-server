package cn.asany.nuwa.app.service;

import cn.asany.nuwa.app.dao.ApplicationMenuDao;
import cn.asany.nuwa.app.domain.ApplicationMenu;
import java.util.*;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.ognl.OgnlUtil;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

/**
 * 应用菜单服务
 *
 * @author limaofeng
 */
@Service
public class ApplicationMenuService {

  private final ApplicationMenuDao menuDao;

  public ApplicationMenuService(ApplicationMenuDao menuDao) {
    this.menuDao = menuDao;
  }

  public List<ApplicationMenu> findAll(Long appId) {
    return this.menuDao.findAll(PropertyFilter.builder().equal("application", appId).build());
  }

  public ApplicationMenu get(Long id) {
    return this.menuDao.getReferenceById(id);
  }

  public ApplicationMenu create(ApplicationMenu menu) {
    OgnlUtil ognlUtil = OgnlUtil.getInstance();
    Long parentId = ognlUtil.getValue("parent.id", menu);

    // 计算 Index
    int index = menu.getIndex();
    List<ApplicationMenu> siblings = siblings(menu);
    if (index >= siblings.size()) {
      menu.setIndex(siblings.size());
    } else {
      for (ApplicationMenu item : siblings.subList(index, siblings.size())) {
        item.setIndex(item.getIndex() + 1);
        this.menuDao.update(item);
      }
    }
    // 计算 Level
    menu.setLevel(parentId == null ? 1 : this.menuDao.getReferenceById(parentId).getLevel() + 1);
    return this.menuDao.save(menu);
  }

  public ApplicationMenu update(Long id, ApplicationMenu menu, Boolean merge) {
    OgnlUtil ognlUtil = OgnlUtil.getInstance();

    menu.setId(id);

    ApplicationMenu beforeMenu = this.menuDao.getReferenceById(id);

    if (hasModifyIndex(menu, beforeMenu)) {
      // 计算 Index
      List<ApplicationMenu> siblings = siblings(beforeMenu);

      if (menu.getIndex() >= siblings.size()) {
        menu.setIndex(siblings.size() - 1);
      }

      int oldIndex = ognlUtil.getValue("index", beforeMenu);
      int newIndex = ognlUtil.getValue("index", menu);

      int startIndex = Math.min(oldIndex, newIndex);
      int endIndex = Math.max(oldIndex, newIndex);

      for (ApplicationMenu item : siblings.subList(startIndex, endIndex + 1)) {
        if (item.getId().equals(menu.getId())) {
          continue;
        }
        item.setIndex(item.getIndex() + (oldIndex > newIndex ? 1 : -1));
        this.menuDao.update(item);
      }
    } else if (hasModifyParent(menu, beforeMenu)) {
      Long parentId = ognlUtil.getValue("parent.id", menu);
      Long beforeParentId = ognlUtil.getValue("parent.id", beforeMenu);
      // 移入
      locomotion(beforeMenu.getApplication().getId(), parentId, menu, true);
      // 移出
      locomotion(beforeMenu.getApplication().getId(), beforeParentId, beforeMenu, false);

      menu.setLevel(parentId == null ? 1 : this.menuDao.getReferenceById(parentId).getLevel() + 1);
    }
    return this.menuDao.update(menu, merge);
  }

  private void locomotion(Long appId, Long parentId, ApplicationMenu menu, boolean join) {
    List<ApplicationMenu> siblings = siblings(appId, parentId);
    for (ApplicationMenu item : siblings.subList(menu.getIndex(), siblings.size())) {
      if (item.getId().equals(menu.getId())) {
        continue;
      }
      item.setIndex(item.getIndex() + (join ? 1 : -1));
      this.menuDao.update(item);
    }
  }

  private boolean hasModifyParent(ApplicationMenu menu, ApplicationMenu beforeMenu) {
    OgnlUtil ognlUtil = OgnlUtil.getInstance();

    Long oldParentId = ognlUtil.getValue("parent.id", beforeMenu);
    Long newParentId = ognlUtil.getValue("parent.id", menu);

    if (newParentId == null && oldParentId != null) {
      return true;
    }
    if (newParentId != null && oldParentId == null) {
      return true;
    }
    return !Objects.equals(newParentId, oldParentId);
  }

  private boolean hasModifyIndex(ApplicationMenu menu, ApplicationMenu beforeMenu) {
    OgnlUtil ognlUtil = OgnlUtil.getInstance();
    if (hasModifyParent(menu, beforeMenu)) {
      return false;
    }

    int oldIndex = ognlUtil.getValue("index", beforeMenu);
    int newIndex = ognlUtil.getValue("index", menu);

    return oldIndex != newIndex;
  }

  private List<ApplicationMenu> siblings(ApplicationMenu menu) {
    return siblings(
        menu.getApplication().getId(), menu.getParent() != null ? menu.getParent().getId() : null);
  }

  private List<ApplicationMenu> siblings(Long appId, Long parentId) {
    PropertyFilterBuilder builder = PropertyFilter.builder();
    builder.equal("application.id", appId);
    if (parentId != null) {
      builder.equal("parent.id", parentId);
    } else {
      builder.isNull("parent");
    }
    return this.menuDao.findAll(builder.build(), Sort.by("index").ascending());
  }

  public long delete(Long... ids) {
    Set<ApplicationMenu> menus = new HashSet<>();
    for (Long id : ids) {
      Optional<ApplicationMenu> optional = this.menuDao.findById(id);
      if (!optional.isPresent()) {
        continue;
      }
      menus.add(optional.get());
    }
    Set<Long> parentIds = new HashSet<>();
    menus.forEach(
        item -> {
          if (item.getParent() == null) {
            parentIds.add(0L);
          } else {
            parentIds.add(item.getParent().getId());
          }
        });
    if (!menus.isEmpty()) {
      this.menuDao.deleteAllById(ObjectUtil.toFieldList(menus, "id", new ArrayList<>()));
    }
    // 重新计算排序值
    for (Long parentId : parentIds) {
      PropertyFilterBuilder builder = PropertyFilter.builder();
      if (Long.valueOf(0).equals(parentId)) {
        builder.isNull("parent");
      } else {
        builder.equal("parent.id", parentId);
      }
      List<ApplicationMenu> siblings =
          this.menuDao.findAll(builder.build(), Sort.by("index").ascending());
      int i = 0;
      for (ApplicationMenu item : siblings) {
        item.setIndex(i++);
        this.menuDao.update(item);
      }
    }
    return menus.size();
  }
}
