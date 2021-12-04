package cn.asany.nuwa.app.service;

import cn.asany.nuwa.app.bean.ApplicationMenu;
import cn.asany.nuwa.app.dao.ApplicationMenuDao;
import java.util.*;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
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
    return this.menuDao.getById(id);
  }

  public ApplicationMenu create(ApplicationMenu menu) {
    // 计算 Index
    PropertyFilterBuilder builder = PropertyFilter.builder();
    builder.equal("application.id", menu.getApplication().getId());
    if (menu.getParent() != null) {
      builder.equal("parent.id", menu.getParent().getId());
    } else {
      builder.isNull("parent");
    }
    int index = menu.getIndex();
    List<ApplicationMenu> siblings =
        this.menuDao.findAll(builder.build(), Sort.by("index").ascending());
    if (index >= siblings.size()) {
      menu.setIndex(siblings.size());
    } else {
      for (ApplicationMenu item : siblings.subList(index, siblings.size())) {
        item.setIndex(item.getIndex() + 1);
        this.menuDao.update(item);
      }
    }
    return this.menuDao.save(menu);
  }

  public ApplicationMenu update(Long id, ApplicationMenu menu, Boolean merge) {
    menu.setId(id);
    return this.menuDao.update(menu, merge);
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
