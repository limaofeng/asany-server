package cn.asany.workflow.security.graphql;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import cn.asany.pm.security.bean.GrantPermission;
import cn.asany.pm.security.bean.enums.SecurityType;
import cn.asany.pm.security.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng@msn.com @ClassName: PermissionGraphQLMutationResolver @Description: (这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
@Component("issuePermissionGraphQLMutationResolver")
public class PermissionGraphQLMutationResolver implements GraphQLMutationResolver {

  @Autowired private PermissionService service;

  /**
   * @ClassName: PermissionGraphQLMutationResolver @Description: 给某个权限授予给某一类人的某个人
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public GrantPermission grantIssuePermission(
      Long scheme, Long permission, SecurityType securityType, String value) {
    return service.grantPermission(scheme, permission, securityType, value);
  }

  /**
   * @ClassName: PermissionGraphQLMutationResolver @Description: 在权限列表中，删除某个用户拥有的某个权限
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Boolean removeIssueGrantPermission(Long id) {
    return service.removeGrantPermission(id);
  }
}
