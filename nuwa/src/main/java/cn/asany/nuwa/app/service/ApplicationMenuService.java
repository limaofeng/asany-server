package cn.asany.nuwa.app.service;

import cn.asany.nuwa.app.dao.ApplicationMenuDao;
import cn.asany.nuwa.app.domain.ApplicationMenu;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.dao.jpa.PropertyFilterBuilder;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.SortNodeLoader;
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
  private final ApplicationService applicationService;

  public ApplicationMenuService(ApplicationMenuDao menuDao, ApplicationService applicationService) {
    this.menuDao = menuDao;
    this.applicationService = applicationService;
  }

  public List<ApplicationMenu> findAll(Long appId) {
    return this.menuDao.findAll(PropertyFilter.builder().equal("application", appId).build());
  }

  public ApplicationMenu get(Long id) {
    return this.menuDao.getReferenceById(id);
  }

  public ApplicationMenu create(ApplicationMenu menu) {
    ObjectUtil.resort(menu, sortNodeLoader, this.menuDao::update);

    return this.menuDao.save(menu);
  }

  public ApplicationMenu update(Long id, ApplicationMenu menu, Boolean merge) {
    menu.setId(id);

    ApplicationMenu old = this.menuDao.getReferenceById(id);
    menu.setApplication(old.getApplication());

    ObjectUtil.resort(menu, sortNodeLoader, this.menuDao::update);

    applicationService.clearAppCache(menu.getApplication().getId());

    return this.menuDao.update(menu, merge);
  }

  public void delete(Long id) {
    Optional<ApplicationMenu> optional = this.menuDao.findById(id);
    if (!optional.isPresent()) {
      return;
    }
    this.menuDao.deleteById(id);

    ApplicationMenu menu = optional.get();
    ObjectUtil.resort(menu, sortNodeLoader, this.menuDao::update);

    applicationService.clearAppCache(menu.getApplication().getId());
  }

  private final SortNodeLoader<ApplicationMenu> sortNodeLoader =
      new ApplicationMenuService.MenuSortNodeLoader();

  private class MenuSortNodeLoader implements SortNodeLoader<ApplicationMenu> {
    @Override
    public List<ApplicationMenu> getAll(Serializable parentId, ApplicationMenu route) {
      PropertyFilterBuilder builder = PropertyFilter.builder();
      if (route.getParent() == null) {
        builder.isNull("parent");
        builder.equal("application.id", route.getApplication().getId());
      } else {
        builder.equal("parent.id", parentId);
      }
      return ApplicationMenuService.this.menuDao.findAll(
          builder.build(), Sort.by("index").ascending());
    }

    @Override
    public ApplicationMenu load(Serializable id) {
      if (id == null) {
        return null;
      }
      return ApplicationMenuService.this.menuDao.findById((Long) id).orElse(null);
    }
  }
}
