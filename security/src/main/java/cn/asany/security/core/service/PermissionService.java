package cn.asany.security.core.service;

import cn.asany.security.core.dao.GrantPermissionDao;
import cn.asany.security.core.dao.PermissionDao;
import cn.asany.security.core.dao.PermissionTypeDao;
import cn.asany.security.core.domain.Permission;
import cn.asany.security.core.domain.PermissionType;
import cn.asany.security.core.domain.enums.PermissionGrantType;
import cn.asany.security.core.domain.enums.ResourceType;
import cn.asany.security.core.exception.ValidDataException;
import cn.asany.security.core.service.dto.ImportPermission;
import cn.asany.security.core.util.GrantPermissionUtils;
import java.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.common.toys.CompareResults;
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

  private final PermissionTypeDao permissionTypeDao;

  private final GrantPermissionDao grantPermissionDao;

  @Autowired
  public PermissionService(
      PermissionDao permissionDao,
      PermissionTypeDao permissionTypeDao,
      GrantPermissionDao grantPermissionDao) {
    this.permissionDao = permissionDao;
    this.permissionTypeDao = permissionTypeDao;
    this.grantPermissionDao = grantPermissionDao;
  }

  public Page<Permission> findPage(Pageable pageable, PropertyFilter filter) {
    return this.permissionDao.findPage(pageable, filter);
  }

  public List<Permission> findAll(PropertyFilter filter, Sort orderBy) {
    return this.permissionDao.findAll(filter, orderBy);
  }

  public Permission save(Permission permission) {
    if (permissionDao.existsById(permission.getId())) {
      throw new ValidDataException(ValidDataException.PERMISSION_EXISTS, permission.getId());
    }
    return this.permissionDao.save(permission);
  }

  public List<Permission> importPermission(ImportPermission importPermission) {

    Optional<PermissionType> optionalType =
        this.permissionTypeDao.findById(importPermission.getId());

    PermissionType type =
        optionalType.orElseGet(
            () ->
                this.permissionTypeDao.save(
                    PermissionType.builder()
                        .id(importPermission.getId())
                        .name(importPermission.getName())
                        .description(importPermission.getDescription())
                        .index((int) (this.permissionTypeDao.count() + 1))
                        .build()));

    List<Permission> oldPermissions =
        ObjectUtil.defaultValue(type.getPermissions(), new ArrayList<>());

    List<Permission> permissions = importPermission.getPermissions();

    ObjectUtil.recursive(
        permissions,
        (item, context) -> {
          Permission parent = (Permission) context.getParent();
          item.setParent(parent);
          item.setType(type);
          item.setEnabled(Boolean.TRUE);
          item.setTokenTypes(new HashSet<>());
          item.setIndex(context.getIndex());
          item.setGrantType(PermissionGrantType.GENERAL);
          return item;
        },
        "scopes");

    List<Permission> allPermissions = ObjectUtil.flat(permissions, "scopes", "parent");

    CompareResults<Permission> results =
        ObjectUtil.compare(oldPermissions, allPermissions, Comparator.comparing(Permission::getId));

    allPermissions.forEach(item -> item.setScopes(new ArrayList<>()));

    this.permissionDao.saveAll(allPermissions);

    if (!results.getExceptA().isEmpty()) {
      this.permissionDao.deleteAll(results.getExceptA());
    }

    return allPermissions;
  }

  public Permission update(String id, Boolean merge, Permission permission) {
    if (!permissionDao.existsById(id)) {
      throw new ValidDataException(ValidDataException.PERMISSION_NOTEXISTS, id);
    }
    permission.setId(id);
    return permissionDao.save(permission);
  }

  public void delete(String... ids) {
    for (String id : ids) {
      if (!permissionDao.existsById(id)) {
        throw new ValidDataException(ValidDataException.PERMISSION_NOTEXISTS, id);
      }
      //      grantPermissionDao.deleteGrantPermissionByPermissionId(id);
      grantPermissionDao.flush();
      permissionDao.deleteById(id);
    }
  }

  private List<Permission> loadPermissionByUrl() {
    return this.permissionDao.findAll(
        (root, query, builder) ->
            builder.and(builder.equal(root.get("resource.type"), ResourceType.url)));
  }

  public List<Permission> findByRoleId(String roleId) {
    return this.permissionDao.findAll(
        (root, query, builder) -> builder.equal(root.get("roles.id"), roleId));
  }

  public Page<PermissionType> findTypePage(Pageable pageable, PropertyFilter filter) {
    return this.permissionTypeDao.findPage(pageable, filter);
  }

  public PermissionType savePermissionType(PermissionType permissionType) {
    if (permissionTypeDao.existsById(permissionType.getId())) {
      throw new ValidDataException(ValidDataException.PERMITYPE_EXISTS, permissionType.getId());
    }
    return permissionTypeDao.save(permissionType);
  }

  public PermissionType updatePermissionType(
      String id, Boolean merge, PermissionType permissionType) {
    if (!permissionTypeDao.existsById(id)) {
      throw new ValidDataException(ValidDataException.PERMITYPE_NOTEXISTS, id);
    }
    permissionType.setId(id);
    return permissionTypeDao.save(permissionType);
  }

  public void deletePermissionType(String id) {
    if (!permissionTypeDao.existsById(id)) {
      throw new ValidDataException(ValidDataException.PERMITYPE_NOTEXISTS, id);
    }
    PermissionType permissionType = permissionTypeDao.getReferenceById(id);
    if (CollectionUtils.isEmpty(permissionType.getPermissions())) {
      permissionTypeDao.delete(permissionType);
    } else {
      throw new ValidDataException(ValidDataException.PERMITYPE_HAS_PERMISSIONS, id);
    }
  }

  public List<Permission> permission(String resourceType, String resourceId) {
    return GrantPermissionUtils.getPermissions(resourceType, resourceId);
  }
}
