package cn.asany.nuwa.app.service;

import cn.asany.nuwa.app.bean.ApplicationMenu;
import cn.asany.nuwa.app.dao.ApplicationMenuDao;
import java.util.List;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
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
    return this.menuDao.save(menu);
  }

  public ApplicationMenu update(Long id, ApplicationMenu menu, Boolean merge) {
    menu.setId(id);
    return this.menuDao.update(menu, merge);
  }

  public void delete(Long id) {
    this.menuDao.deleteById(id);
  }
}
