package cn.asany.security.core.service;

import cn.asany.security.core.dao.GrantPermissionDao;
import cn.asany.security.core.dao.PermissionStatementDao;
import cn.asany.security.core.domain.PermissionStatement;
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

  private final PermissionStatementDao permissionStatementDao;

  private final GrantPermissionDao grantPermissionDao;

  @Autowired
  public PermissionService(
      PermissionStatementDao permissionStatementDao, GrantPermissionDao grantPermissionDao) {
    this.permissionStatementDao = permissionStatementDao;
    this.grantPermissionDao = grantPermissionDao;
  }

  public Page<PermissionStatement> findPage(Pageable pageable, PropertyFilter filter) {
    return this.permissionStatementDao.findPage(pageable, filter);
  }

  public List<PermissionStatement> findAll(PropertyFilter filter, Sort orderBy) {
    return this.permissionStatementDao.findAll(filter, orderBy);
  }

  public PermissionStatement save(PermissionStatement permissionStatement) {
    //    if (permissionDao.existsById(permission.getId())) {
    //      throw new ValidDataException(ValidDataException.PERMISSION_EXISTS, permission.getId());
    //    }
    return this.permissionStatementDao.save(permissionStatement);
  }

  public PermissionStatement update(
      Long id, Boolean merge, PermissionStatement permissionStatement) {
    if (!permissionStatementDao.existsById(id)) {
      throw new ValidDataException(ValidDataException.PERMISSION_NOTEXISTS, id);
    }
    permissionStatement.setId(id);
    return permissionStatementDao.save(permissionStatement);
  }

  public void delete(Long... ids) {
    for (Long id : ids) {
      if (!permissionStatementDao.existsById(id)) {
        throw new ValidDataException(ValidDataException.PERMISSION_NOTEXISTS, id);
      }
      //      grantPermissionDao.deleteGrantPermissionByPermissionId(id);
      grantPermissionDao.flush();
      permissionStatementDao.deleteById(id);
    }
  }

  //  private List<Permission> loadPermissionByUrl() {
  //    return this.permissionDao.findAll(
  //        (root, query, builder) ->
  //            builder.and(builder.equal(root.get("resource.type"), ResourceType.url)));
  //  }

  public List<PermissionStatement> findByRoleId(String roleId) {
    return this.permissionStatementDao.findAll(
        (root, query, builder) -> builder.equal(root.get("roles.id"), roleId));
  }

  //  public List<Permission> permission(Long resourceType, String resourceId) {
  //    return GrantPermissionUtils.getPermissions(resourceType, resourceId);
  //  }
}
