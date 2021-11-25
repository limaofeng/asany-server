package cn.asany.ui.resources.service;

import cn.asany.ui.resources.bean.Component;
import cn.asany.ui.resources.dao.ComponentDao;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.stereotype.Service;

/**
 * 组件服务
 *
 * @author limaofeng
 */
@Service
public class ComponentService {

  private final ComponentDao componentDao;

  public ComponentService(ComponentDao componentDao) {
    this.componentDao = componentDao;
  }

  public Pager<Component> findPager(Pager<Component> pager, List<PropertyFilter> filters) {
    return componentDao.findPager(pager, filters);
  }

  public Component save(Component component) {
    return componentDao.save(component);
  }

  public Component update(Long id, Component component, Boolean merge) {
    component.setId(id);
    return this.componentDao.update(component, merge);
  }

  public void delete(Long id) {
    this.componentDao.deleteById(id);
  }

  public Optional<Component> findById(Long id) {
    return this.componentDao.findById(id);
  }
}
