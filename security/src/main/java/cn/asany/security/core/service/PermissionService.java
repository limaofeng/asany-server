package cn.asany.security.core.service;

import cn.asany.security.core.dao.GrantPermissionDao;
import cn.asany.security.core.dao.PermissionDao;
import cn.asany.security.core.domain.Permission;
import cn.asany.security.core.exception.ValidDataException;
import java.util.*;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** @author limaofeng */
@Service
@Transactional
public class PermissionService {

  private final PermissionDao permissionDao;

  private final GrantPermissionDao grantPermissionDao;

  @Autowired
  public PermissionService(PermissionDao permissionDao, GrantPermissionDao grantPermissionDao) {
    this.permissionDao = permissionDao;
    this.grantPermissionDao = grantPermissionDao;
  }

  public Page<Permission> findPage(Pageable pageable, PropertyFilter filter) {
    return this.permissionDao.findPage(pageable, filter);
  }

  public List<Permission> findAll(PropertyFilter filter, Sort orderBy) {
    return this.permissionDao.findAll(filter, orderBy);
  }

  public Permission save(Permission permission) {
    //    if (permissionDao.existsById(permission.getId())) {
    //      throw new ValidDataException(ValidDataException.PERMISSION_EXISTS, permission.getId());
    //    }
    return this.permissionDao.save(permission);
  }

  public Permission update(Long id, Boolean merge, Permission permission) {
    if (!permissionDao.existsById(id)) {
      throw new ValidDataException(ValidDataException.PERMISSION_NOTEXISTS, id);
    }
    permission.setId(id);
    return permissionDao.save(permission);
  }

  public void delete(Long... ids) {
    for (Long id : ids) {
      if (!permissionDao.existsById(id)) {
        throw new ValidDataException(ValidDataException.PERMISSION_NOTEXISTS, id);
      }
      //      grantPermissionDao.deleteGrantPermissionByPermissionId(id);
      grantPermissionDao.flush();
      permissionDao.deleteById(id);
    }
  }

  //  private List<Permission> loadPermissionByUrl() {
  //    return this.permissionDao.findAll(
  //        (root, query, builder) ->
  //            builder.and(builder.equal(root.get("resource.type"), ResourceType.url)));
  //  }

  public List<Permission> findByRoleId(String roleId) {
    return this.permissionDao.findAll(
        (root, query, builder) -> builder.equal(root.get("roles.id"), roleId));
  }

  //  public List<Permission> permission(Long resourceType, String resourceId) {
  //    return GrantPermissionUtils.getPermissions(resourceType, resourceId);
  //  }
}
