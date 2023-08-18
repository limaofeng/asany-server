package cn.asany.security.core.service;

import cn.asany.security.core.dao.PermissionDao;
import cn.asany.security.core.domain.Grantee;
import cn.asany.security.core.domain.Permission;
import cn.asany.security.core.domain.enums.GranteeType;
import cn.asany.security.core.exception.ValidDataException;
import java.util.*;
import java.util.stream.Collectors;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.security.core.GrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 权限管理
 *
 * @author limaofeng
 */
@Service
@Transactional
public class PermissionService {

  private final PermissionDao permissionDao;

  private final UserService userService;

  @Autowired
  public PermissionService(UserService userService, PermissionDao permissionDao) {
    this.userService = userService;
    this.permissionDao = permissionDao;
  }

  public Page<Permission> findPage(Pageable pageable, PropertyFilter filter) {
    return this.permissionDao.findPage(pageable, filter);
  }

  public List<Permission> findAll(PropertyFilter filter, Sort orderBy) {
    return this.permissionDao.findAll(filter, orderBy);
  }

  public Permission save(Permission permissionStatement) {
    //    if (permissionDao.existsById(permission.getId())) {
    //      throw new ValidDataException(ValidDataException.PERMISSION_EXISTS, permission.getId());
    //    }
    return this.permissionDao.save(permissionStatement);
  }

  public Permission update(Long id, Permission permission, Boolean merge) {
    if (!permissionDao.existsById(id)) {
      throw new ValidDataException(ValidDataException.PERMISSION_NOTEXISTS, id);
    }
    permission.setId(id);
    return permissionDao.update(permission, merge);
  }

  public void delete(Long... ids) {
    for (Long id : ids) {
      if (!permissionDao.existsById(id)) {
        throw new ValidDataException(ValidDataException.PERMISSION_NOTEXISTS, id);
      }
      //      grantPermissionDao.deleteGrantPermissionByPermissionId(id);
      //      grantPermissionDao.flush();
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

  public List<Permission> findAll(GranteeType granteeType, String code) {
    return null;
  }

  public Set<String> getPermissions(Long id) {
    Grantee userGrantee = Grantee.user(id);

    Collection<GrantedAuthority> authorities = this.userService.getGrantedAuthorities(id);

    // 直接分配给用户的权限
    List<PropertyFilter> filters = new ArrayList<>();
    filters.add(
        PropertyFilter.newFilter()
            .equal("grantee.type", userGrantee.getType())
            .equal("grantee.value", userGrantee.getValue()));

    // 通过用户组与角色授予的权限
    for (GrantedAuthority authority : authorities) {
      Grantee grantee = Grantee.valueOf(authority.getAuthority());
      filters.add(
          PropertyFilter.newFilter()
              .equal("grantee.type", grantee.getType())
              .equal("grantee.value", grantee.getValue()));
    }

    return this.permissionDao
        .findAll(PropertyFilter.newFilter().or(filters.toArray(new PropertyFilter[0])))
        .stream()
        .map(item -> item.getPolicy().getName())
        .collect(Collectors.toSet());
  }

  //  public List<Permission> permission(Long resourceType, String resourceId) {
  //    return GrantPermissionUtils.getPermissions(resourceType, resourceId);
  //  }
}
