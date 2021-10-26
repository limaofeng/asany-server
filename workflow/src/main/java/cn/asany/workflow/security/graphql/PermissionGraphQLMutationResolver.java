package cn.asany.workflow.security.graphql;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import net.whir.hos.issue.security.bean.GrantPermission;
import net.whir.hos.issue.security.bean.enums.SecurityType;
import net.whir.hos.issue.security.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author penghanying @ClassName: PermissionGraphQLMutationResolver @Description: (这里用一句话描述这个类的作用)
 * @date 2019/5/31
 */
@Component("issuePermissionGraphQLMutationResolver")
public class PermissionGraphQLMutationResolver implements GraphQLMutationResolver {

  @Autowired private PermissionService service;

  /**
   * @ClassName: PermissionGraphQLMutationResolver @Description: 给某个权限授予给某一类人的某个人
   *
   * @author penghanying
   * @date 2019/5/31
   */
  public GrantPermission grantIssuePermission(
      Long scheme, Long permission, SecurityType securityType, String value) {
    return service.grantPermission(scheme, permission, securityType, value);
  }

  /**
   * @ClassName: PermissionGraphQLMutationResolver @Description: 在权限列表中，删除某个用户拥有的某个权限
   *
   * @author penghanying
   * @date 2019/5/31
   */
  public Boolean removeIssueGrantPermission(Long id) {
    return service.removeGrantPermission(id);
  }
}
