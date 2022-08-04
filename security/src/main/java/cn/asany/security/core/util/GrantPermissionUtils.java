package cn.asany.security.core.util;

import cn.asany.base.common.SecurityScope;
import cn.asany.base.common.SecurityType;
import cn.asany.security.core.domain.GrantPermission;
import cn.asany.security.core.domain.Permission;
import cn.asany.security.core.domain.User;
import cn.asany.security.core.graphql.input.PermissionInput;
import cn.asany.security.core.service.GrantPermissionService;
import cn.asany.security.core.service.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jfantasy.framework.spring.SpringBeanUtils;
import org.jfantasy.framework.util.common.StringUtil;

/**
 * 授予权限
 *
 * @author limaofeng
 * @version V1.0
 * @date 2022/7/28 9:12 9:12
 */
public class GrantPermissionUtils {

  private static GrantPermissionService grantPermissionService;

  private static GrantPermissionService getGrantPermissionService() {
    if (grantPermissionService == null) {
      grantPermissionService = SpringBeanUtils.getBeanByType(GrantPermissionService.class);
    }
    return grantPermissionService;
  }

  public static List<GrantPermission> getGrantPermissions(List<GrantPermission> grants) {
    return grants.stream()
        .map(
            item ->
                GrantPermission.builder()
                    .permission(item.getPermission())
                    //                    .securityType(item.getSecurityType())
                    .value(item.getValue())
                    //                    .resource(item.getResource())
                    .build())
        .collect(Collectors.toList());
  }

  public static List<GrantPermission> getGrantPermissions(
      List<GrantPermission> grants, String permissionKey) {
    Stream<GrantPermission> stream = grants.stream();
    if (StringUtil.isNotBlank(permissionKey)) {
      stream =
          stream.filter(
              item ->
                  item.getPermission().getEnabled()
                      && item.getPermission().getId().equals(permissionKey));
    }
    return stream
        .map(
            item ->
                GrantPermission.builder()
                    .permission(item.getPermission())
                    //                    .securityType(item.getSecurityType())
                    .value(item.getValue())
                    .build())
        .collect(Collectors.toList());
  }

  public static Stream<User> getUsersByPermission(
      List<Permission> permissions, String permissionKey) {
    Stream<Permission> stream = permissions.stream();
    if (StringUtil.isNotBlank(permissionKey)) {
      stream = stream.filter(item -> item.getEnabled() && item.getId().equals(permissionKey));
    }
    List<User> users = new ArrayList<>();
    //    stream.forEach(
    //        p -> {
    //          users.addAll(
    //              p.getGrants().stream()
    //                  .filter(item -> item.getSecurityType() == SecurityType.user)
    //                  .filter(item -> StringUtil.isNotBlank(item.getValue()))
    //                  .map(item -> getUserService().get(Long.valueOf(item.getValue())))
    //                  .filter(Optional::isPresent)
    //                  .map(Optional::get)
    //                  .collect(Collectors.toList()));
    //        });
    return users.stream();
  }

  private static UserService getUserService() {
    return SpringBeanUtils.getBeanByType(UserService.class);
  }

  public static List<Permission> getPermissions(List<Permission> grants, String permissionKey) {
    if (StringUtil.isBlank(permissionKey)) {
      return grants;
    }
    return grants.stream()
        .filter(item -> item.getId().equals(permissionKey))
        .collect(Collectors.toList());
  }

  public static List<GrantPermission> getGrantPermissions(String resourceType, Long resource) {
    GrantPermissionService grantPermissionService = getGrantPermissionService();
    return grantPermissionService.getGrantPermissions(resourceType, resource.toString());
  }

  public static List<Permission> getPermissions(String resourceType, String resource) {
    GrantPermissionService grantPermissionService = getGrantPermissionService();
    return grantPermissionToPermission(
        grantPermissionService.getGrantPermissions(resourceType, resource));
  }

  public static List<Permission> getPermissions(String resourceType, Long resource) {
    GrantPermissionService grantPermissionService = getGrantPermissionService();
    return grantPermissionToPermission(
        grantPermissionService.getGrantPermissions(resourceType, resource.toString()));
  }

  public static List<GrantPermission> getGrantPermissions(SecurityType securityType, String value) {
    GrantPermissionService grantPermissionService = getGrantPermissionService();
    return grantPermissionService.getGrantPermissions(securityType, value);
  }

  public static List<Permission> updateGrantPermissions(
      String resourceType, Long resource, List<PermissionInput> inputs) {
    // 转换数据
    List<GrantPermission> grants = new ArrayList<>();
    inputs.forEach(
        item -> {
          item.getGrants()
              .forEach(
                  grant -> {
                    SecurityScope securityScope = SecurityScope.newInstance(grant);
                    grants.add(
                        GrantPermission.builder()
                            //                            .value(securityScope.getValue())
                            //                            .resource(resource.toString())
                            //                            .securityType(securityScope.getType())
                            .permission(Permission.builder().id(item.getPermission()).build())
                            .build());
                  });
        });
    GrantPermissionService grantPermissionService = getGrantPermissionService();
    // 获取之前的权限
    List<GrantPermission> preGrants =
        GrantPermissionUtils.getGrantPermissions(resourceType, resource);
    cleanGrantPermission(preGrants, grants);
    // 拼装返回结果
    List<GrantPermission> grantPermissions =
        grants.stream()
            .map(grant -> grantPermissionService.saveGrantPermission(resource.toString(), grant))
            .collect(Collectors.toList());
    return grantPermissionToPermission(grantPermissions);
  }

  private static List<Permission> grantPermissionToPermission(List<GrantPermission> grants) {
    List<Permission> permissions = new ArrayList<>();
    grants.stream()
        .forEach(
            item -> {
              Optional<Permission> optional =
                  permissions.stream()
                      .filter(p -> p.getId().equals(item.getPermission().getId()))
                      .findFirst();
              if (optional.isPresent()) {
                optional.get().getGrants().add(item);
              } else {
                Permission permission = item.getPermission();
                permission.setGrants(new ArrayList<>());
                permission.getGrants().add(item);
                permissions.add(permission);
              }
            });
    return permissions;
  }

  private static void cleanGrantPermission(
      List<GrantPermission> preGrants, List<GrantPermission> grants) {
    // 删除已经被删除的权限
    preGrants.stream()
        .filter(grant -> !grants.stream().anyMatch(item -> item.equals(grant)))
        .forEach(
            grant -> {
              grantPermissionService.deleteGrantPermission(grant);
            });
  }

  public static List<GrantPermission> allocation(
      SecurityType securityType, String value, List<GrantPermission> grants) {
    // 获取之前的权限
    List<GrantPermission> preGrants = GrantPermissionUtils.getGrantPermissions(securityType, value);
    cleanGrantPermission(preGrants, grants);
    return grants.stream()
        .map(grant -> grantPermissionService.saveGrantPermission(securityType, value, grant))
        .collect(Collectors.toList());
  }
}
