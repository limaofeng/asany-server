package cn.asany.security.core.service;

import cn.asany.security.core.bean.RoleSpace;
import cn.asany.security.core.dao.RoleSpaceDao;
import java.util.List;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleScopeService {

  @Autowired private RoleSpaceDao roleSpaceDao;

  public Pager<RoleSpace> findPager(Pager<RoleSpace> pager, List<PropertyFilter> filters) {
    return this.roleSpaceDao.findPager(pager, filters);
  }

  public RoleSpace get(String id) {
    return this.roleSpaceDao.getById(id);
  }

  public RoleSpace save(RoleSpace scope) {
    return roleSpaceDao.save(scope);
  }

  public List<RoleSpace> findAll(List<PropertyFilter> filters) {
    return roleSpaceDao.findAll(filters);
  }

  public RoleSpace update(String id, boolean merge, RoleSpace scope) {
    scope.setId(id);
    return this.roleSpaceDao.update(scope, merge);
  }
}
