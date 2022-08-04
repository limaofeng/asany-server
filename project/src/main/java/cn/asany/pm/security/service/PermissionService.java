package cn.asany.pm.security.service;

import cn.asany.pm.security.bean.GrantPermission;
import cn.asany.pm.security.bean.Permission;
import cn.asany.pm.security.bean.PermissionScheme;
import cn.asany.pm.security.bean.enums.SecurityType;
import cn.asany.pm.security.dao.GrantPermissionDao;
import cn.asany.pm.security.dao.PermissionDao;
import cn.asany.pm.security.dao.PermissionSchemeDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author limaofeng@msn.com @ClassName: PermissionService @Description: 权限Service(这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
@Service("issuePermissionsService")
@Transactional(rollbackFor = Exception.class)
public class PermissionService {

  @Autowired private PermissionDao permissionDao;

  @Autowired private PermissionSchemeDao permissionSchemeDao;

  @Autowired private GrantPermissionDao grantPermissionDao;

  /**
   * @ClassName: PermissionsService @Description: 给某个权限授予给某一类人的某个人
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public GrantPermission grantPermission(
      Long scheme, Long permission, SecurityType securityType, String value) {
    GrantPermission grantPermission = new GrantPermission();
    Permission permissions = permissionDao.findById(permission).orElse(null);
    if (permissions != null) {
      grantPermission.setPermission(permissions);
    }
    PermissionScheme permissionScheme = permissionSchemeDao.findById(scheme).orElse(null);
    if (permissionScheme != null) {
      grantPermission.setScheme(permissionScheme);
    }
    grantPermission.setSecurityType(securityType);
    grantPermission.setValue(value);
    return grantPermissionDao.save(grantPermission);
  }

  /**
   * @ClassName: PermissionsService @Description: 在权限列表中，删除某个用户拥有的某个权限
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Boolean removeGrantPermission(Long id) {
    grantPermissionDao.deleteById(id);
    return true;
  }

  /**
   * @ClassName: PermissionsService @Description: 查询全部权限方案
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public List<PermissionScheme> permissionSchemes() {
    return permissionSchemeDao.findAll();
  }

  /**
   * @ClassName: PermissionsService @Description: 查询某个权限方案的列表
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public PermissionScheme permissionScheme(Long id) {
    return permissionSchemeDao.findById(id).orElse(null);
  }

  /**
   * @ClassName: PermissionsService @Description: 根据权限模板的id，查询全部权限
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public List<GrantPermission> getGrantPermissions(Long scheme, String permission) {
    // 通过权限的code，查询权限的id
    List<Permission> all =
        permissionDao.findAll(Example.of(Permission.builder().code(permission).build()));
    if (all.size() > 0) {
      return grantPermissionDao.findAll(
          Example.of(
              GrantPermission.builder()
                  .scheme(PermissionScheme.builder().id(scheme).build())
                  .permission(all.get(0))
                  .build()));
    }
    return null;
  }
}
