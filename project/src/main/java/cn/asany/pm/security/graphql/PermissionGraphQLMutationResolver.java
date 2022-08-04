package cn.asany.pm.security.graphql;

import cn.asany.pm.security.bean.GrantPermission;
import cn.asany.pm.security.bean.enums.SecurityType;
import cn.asany.pm.security.service.PermissionService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 权限接口
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
@Component("issuePermissionGraphQLMutationResolver")
public class PermissionGraphQLMutationResolver implements GraphQLMutationResolver {

  @Autowired private PermissionService permissionService;

  /**
   * 给某个权限授予给某一类人的某个人
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public GrantPermission grantIssuePermission(
      Long scheme, Long permission, SecurityType securityType, String value) {
    return permissionService.grantPermission(scheme, permission, securityType, value);
  }

  /**
   * 在权限列表中，删除某个用户拥有的某个权限
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Boolean removeIssueGrantPermission(Long id) {
    return permissionService.removeGrantPermission(id);
  }
}
