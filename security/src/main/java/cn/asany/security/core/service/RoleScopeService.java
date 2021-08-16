package cn.asany.security.core.service;

import cn.asany.security.core.bean.RoleScope;
import cn.asany.security.core.dao.RoleScopeDao;
import java.util.List;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleScopeService {

  @Autowired private RoleScopeDao roleScopeDao;

  public Pager<RoleScope> findPager(Pager<RoleScope> pager, List<PropertyFilter> filters) {
    return this.roleScopeDao.findPager(pager, filters);
  }

  public RoleScope get(String id) {
    return this.roleScopeDao.getOne(id);
  }

  public RoleScope save(RoleScope scope) {
    return roleScopeDao.save(scope);
  }

  public List<RoleScope> findAll(List<PropertyFilter> filters) {
    return roleScopeDao.findAll(filters);
  }

  public RoleScope update(String id, boolean merge, RoleScope scope) {
    scope.setId(id);
    return this.roleScopeDao.save(scope);
  }
}
