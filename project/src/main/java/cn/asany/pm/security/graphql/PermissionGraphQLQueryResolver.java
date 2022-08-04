package cn.asany.pm.security.graphql;

import cn.asany.pm.security.bean.PermissionScheme;
import cn.asany.pm.security.service.PermissionService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 权限接口
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
@Component("issuePermissionGraphQLQueryResolver")
public class PermissionGraphQLQueryResolver implements GraphQLQueryResolver {

  private final PermissionService permissionService;

  public PermissionGraphQLQueryResolver(PermissionService permissionService) {
    this.permissionService = permissionService;
  }

  /**
   * 查询全部权限方案
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public List<PermissionScheme> issuePermissionSchemes() {
    return permissionService.permissionSchemes();
  }

  /**
   * 查询某个权限方案的列表
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public PermissionScheme issuePermissionScheme(Long id) {
    return permissionService.permissionScheme(id);
  }
}
