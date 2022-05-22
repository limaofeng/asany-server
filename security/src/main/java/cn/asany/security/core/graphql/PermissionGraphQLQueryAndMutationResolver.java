package cn.asany.security.core.graphql;

import cn.asany.security.core.bean.Permission;
import cn.asany.security.core.graphql.models.PermissionConnection;
import cn.asany.security.core.graphql.models.PermissionFilter;
import cn.asany.security.core.service.PermissionService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PermissionGraphQLQueryAndMutationResolver
    implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final PermissionService permissionService;

  public PermissionGraphQLQueryAndMutationResolver(PermissionService permissionService) {
    this.permissionService = permissionService;
  }

  /** 查询权限 */
  public PermissionConnection permissionsConnection(
      PermissionFilter filter, int page, int pageSize, Sort orderBy) {
    filter = ObjectUtil.defaultValue(filter, new PermissionFilter());
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    return Kit.connection(
        permissionService.findPage(pageable, filter.build()), PermissionConnection.class);
  }

  /** 查询权限 */
  public List<Permission> permissions(PermissionFilter filter, Sort orderBy) {
    filter = ObjectUtil.defaultValue(filter, new PermissionFilter());
    return permissionService.findAll(filter.build(), orderBy);
  }
}
