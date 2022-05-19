package cn.asany.security.core.graphql;

import cn.asany.security.core.bean.Permission;
import cn.asany.security.core.graphql.models.PermissionConnection;
import cn.asany.security.core.graphql.models.PermissionFilter;
import cn.asany.security.core.service.PermissionService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
      PermissionFilter filter, int page, int pageSize, OrderBy orderBy) {
    filter = ObjectUtil.defaultValue(filter, new PermissionFilter());
    Pageable pageable = PageRequest.of(page, pageSize, orderBy.toSort());
    return Kit.connection(
        permissionService.findPage(pageable, filter.build()), PermissionConnection.class);
  }

  /** 查询权限 */
  public List<Permission> permissions(PermissionFilter filter, OrderBy orderBy) {
    filter = ObjectUtil.defaultValue(filter, new PermissionFilter());
    orderBy = ObjectUtil.defaultValue(orderBy, () -> OrderBy.asc("index"));
    return permissionService.findAll(filter.build(), orderBy);
  }
}
