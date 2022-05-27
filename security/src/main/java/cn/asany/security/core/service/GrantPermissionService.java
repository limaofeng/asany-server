package cn.asany.security.core.service;

import cn.asany.base.common.SecurityScope;
import cn.asany.base.common.SecurityType;
import cn.asany.security.core.dao.GrantPermissionDao;
import cn.asany.security.core.dao.PermissionDao;
import cn.asany.security.core.domain.GrantPermission;
import cn.asany.security.core.domain.Permission;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-07-17 10:32
 */
@Service
@Transactional
public class GrantPermissionService {

  @Autowired private GrantPermissionDao grantPermissionDao;
  @Autowired private PermissionDao permissionDao;

  public List<GrantPermission> getGrantPermissions(String resourceType, String resource) {
    return grantPermissionDao.findAll(
        Example.of(
            GrantPermission.builder()
                //
                // .permission(Permission.builder().resourceType(resourceType).build())
                //                .resource(resource)
                .build()));
  }

  public List<GrantPermission> getGrantPermissions(SecurityType securityType, String value) {
    //    return grantPermissionDao.findAll(
    //
    // Example.of(GrantPermission.builder().securityType(securityType).value(value).build()));
    return new ArrayList<>();
  }

  public void deleteGrantPermission(GrantPermission grant) {
    this.grantPermissionDao.deleteById(grant.getId());
  }

  public GrantPermission saveGrantPermission(GrantPermission grant) {
    Optional<GrantPermission> optional = this.grantPermissionDao.findOne(Example.of(grant));
    if (optional.isPresent()) {
      GrantPermission oldGrant = optional.get();
      //      oldGrant.setSecurityType(grant.getSecurityType());
      oldGrant.setValue(grant.getValue());
      //      oldGrant.setResource(grant.getResource());
      return this.grantPermissionDao.save(oldGrant);
    } else {
      grant.setPermission(permissionDao.getOne(grant.getPermission().getId()));
      return this.grantPermissionDao.save(grant);
    }
  }

  public GrantPermission saveGrantPermission(String resource, GrantPermission grant) {
    //    grant.setResource(resource);
    return this.saveGrantPermission(grant);
  }

  public GrantPermission saveGrantPermission(
      SecurityType securityType, String value, GrantPermission grant) {
    //    grant.setSecurityType(securityType);
    //    grant.setValue(value);
    return this.saveGrantPermission(grant);
  }

  public List<GrantPermission> getPermissions(SecurityScope... scopes) {
    return this.grantPermissionDao.findAll(
        (Specification<GrantPermission>)
            (root, query, builder) -> {
              List<Predicate> restrictions = new ArrayList<>();
              for (SecurityScope scope : scopes) {
                restrictions.add(
                    builder.and(
                        builder.equal(root.get("securityType"), scope.getType()),
                        builder.equal(root.get("value"), scope.getValue())));
              }
              return builder.or(restrictions.toArray(new Predicate[restrictions.size()]));
            });
  }

  public List<GrantPermission> getPermissions(
      String resourceType, String permissionKey, SecurityScope... scopes) {
    return this.grantPermissionDao.findAll(
        (Specification<GrantPermission>)
            (root, query, builder) -> {
              List<Predicate> restrictions = new ArrayList<>();
              for (SecurityScope scope : scopes) {
                restrictions.add(
                    builder.and(
                        builder.equal(root.get("securityType"), scope.getType()),
                        builder.equal(root.get("value"), scope.getValue())));
              }
              List<Predicate> ands = new ArrayList<>();
              ands.add(builder.or(restrictions.toArray(new Predicate[restrictions.size()])));
              Path path = root.get("permission");
              if (StringUtil.isNotBlank(resourceType)) {
                ands.add(builder.equal(path.get("resourceType"), resourceType));
              }
              if (StringUtil.isNotBlank(permissionKey)) {
                ands.add(builder.equal(path.get("id"), permissionKey));
              }
              return builder.and(ands.toArray(new Predicate[ands.size()]));
            });
  }

  public List<Permission> getPermissionsByRoleId(String roleId) {
    List<GrantPermission> grantPermissionList = new ArrayList<>();
    //        grantPermissionDao.findGrantPermissionsBySecurityTypeAndValue(SecurityType.role,
    // roleId);
    return grantPermissionList.stream()
        .map(GrantPermission::getPermission)
        .collect(Collectors.toList());
  }

  public List<GrantPermission> getGrantPermissionsByPermissionId(String permissionId) {
    return new ArrayList<>(); // grantPermissionDao.findGrantPermissionsByPermissionId(permissionId);
  }

  /**
   * 分配授权
   *
   * @param permissionIds 权限ids
   * @param entityTypeIds 实体Ids组合（格式：type:Ids;;type:Ids）
   */
  public void assignPermissions(String permissionIds, String entityTypeIds) {
    for (String permissionId : permissionIds.split(",")) {
      assignPermission(permissionId.trim(), entityTypeIds.trim());
    }
  }

  private void assignPermission(String permissionId, String entityTypeIds) {
    String[] typeIds = entityTypeIds.split(";;");
    for (String values : typeIds) {
      // values格式：type:Ids?
      String[] arr = values.split(":");
      String entityType = arr[0];
      // 先删除
      //      grantPermissionDao.deleteGrantPermissionByPermissionIdAndSecurityType(
      //          permissionId, SecurityType.valueOf(entityType));
      if (arr.length != 2) {
        continue;
      }
      String entityIds = arr[1];
      grantEntityPermissions(permissionId, entityType, entityIds);
    }
  }

  public void grantEntityPermissions(String permissionId, String entityType, String entityIds) {
    for (String entityId : entityIds.split(",")) {
      Permission permission = permissionDao.getOne(permissionId);
      grantPermissionDao.save(
          GrantPermission.builder()
              .permission(permission)
              //              .securityType(SecurityType.valueOf(entityType))
              //              .value(entityId)
              .build());
    }
  }
}
