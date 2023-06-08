package cn.asany.security.core.service;

import cn.asany.security.core.dao.RoleSpaceDao;
import cn.asany.security.core.domain.RoleSpace;
import java.util.List;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleScopeService {

  private final RoleSpaceDao roleSpaceDao;

  public RoleScopeService(RoleSpaceDao roleSpaceDao) {
    this.roleSpaceDao = roleSpaceDao;
  }

  public Page<RoleSpace> findPage(Pageable pageable, PropertyFilter filter) {
    return this.roleSpaceDao.findPage(pageable, filter);
  }

  public RoleSpace get(String id) {
    return this.roleSpaceDao.getReferenceById(id);
  }

  public RoleSpace save(RoleSpace scope) {
    return roleSpaceDao.save(scope);
  }

  public List<RoleSpace> findAll(PropertyFilter filter) {
    return roleSpaceDao.findAll(filter);
  }

  public RoleSpace update(Long id, boolean merge, RoleSpace scope) {
    scope.setId(id);
    return this.roleSpaceDao.update(scope, merge);
  }
}
