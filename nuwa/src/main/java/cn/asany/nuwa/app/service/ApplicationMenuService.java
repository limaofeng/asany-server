package cn.asany.nuwa.app.service;

import cn.asany.nuwa.app.dao.ApplicationMenuDao;
import cn.asany.nuwa.app.domain.ApplicationMenu;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.util.common.ObjectUtil;
import net.asany.jfantasy.framework.util.common.SortNodeLoader;
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
    return this.menuDao.findAll(PropertyFilter.newFilter().equal("application", appId));
  }

  public List<ApplicationMenu> findAll(PropertyFilter filter) {
    return this.menuDao.findAll(filter);
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
      PropertyFilter filter = PropertyFilter.newFilter();
      if (route.getParent() == null) {
        filter.isNull("parent");
        filter.equal("application.id", route.getApplication().getId());
      } else {
        filter.equal("parent.id", parentId);
      }
      return ApplicationMenuService.this.menuDao.findAll(filter, Sort.by("index").ascending());
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
