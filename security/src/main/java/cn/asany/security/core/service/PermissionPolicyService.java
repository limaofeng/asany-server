package cn.asany.security.core.service;

import cn.asany.security.core.dao.PermissionDao;
import cn.asany.security.core.dao.PermissionPolicyDao;
import cn.asany.security.core.domain.Permission;
import cn.asany.security.core.domain.enums.GranteeType;
import java.util.Collection;
import java.util.List;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.jfantasy.framework.security.core.GrantedAuthority;
import org.jfantasy.framework.security.core.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

/**
 * 权限策略服务
 *
 * @author limaofeng
 */
@Service
public class PermissionPolicyService {

  private final PermissionPolicyDao permissionPolicyDao;
  private final PermissionDao permissionDao;
  private final GrantPermissionService grantPermissionService;

  public PermissionPolicyService(
      PermissionPolicyDao permissionPolicyDao,
      PermissionDao permissionDao,
      GrantPermissionService grantPermissionService) {
    this.permissionPolicyDao = permissionPolicyDao;
    this.permissionDao = permissionDao;
    this.grantPermissionService = grantPermissionService;
  }

  public List<Permission> loadPolicies(Collection<GrantedAuthority> authorities, String action) {
    PropertyFilter filter = PropertyFilter.newFilter().equal("action", action);
    for (GrantedAuthority authority : authorities) {
      if (authority instanceof SimpleGrantedAuthority) {
        String type = ((SimpleGrantedAuthority) authority).getType();
        String code = ((SimpleGrantedAuthority) authority).getCode();
        filter.or(
            PropertyFilter.newFilter()
                .equal("policy.grantPermissions.granteeType", GranteeType.valueOf(type))
                .equal("policy.grantPermissions.granteeId", code));
      }
    }
    return permissionDao.findAll(filter);
  }
}
