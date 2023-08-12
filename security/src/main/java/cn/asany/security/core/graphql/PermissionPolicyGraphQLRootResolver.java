package cn.asany.security.core.graphql;

import cn.asany.security.core.graphql.input.PermissionPolicyWhereInput;
import cn.asany.security.core.graphql.types.PermissionPolicyConnection;
import cn.asany.security.core.service.PermissionPolicyService;
import org.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * 权限策略
 *
 * @author limaofeng
 */
public class PermissionPolicyGraphQLRootResolver {

  private final PermissionPolicyService permissionPolicyService;

  public PermissionPolicyGraphQLRootResolver(PermissionPolicyService permissionPolicyService) {
    this.permissionPolicyService = permissionPolicyService;
  }

  public PermissionPolicyConnection policies(
    PermissionPolicyWhereInput where, int page, int pageSize, Sort orderBy) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    return Kit.connection(
      permissionPolicyService.findPage(pageable, where.toFilter()),
      PermissionPolicyConnection.class);
  }
}
