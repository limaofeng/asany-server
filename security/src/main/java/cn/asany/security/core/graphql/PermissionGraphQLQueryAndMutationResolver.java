package cn.asany.security.core.graphql;

import cn.asany.security.core.domain.PermissionStatement;
import cn.asany.security.core.graphql.input.PermissionWhereInput;
import cn.asany.security.core.graphql.types.PermissionConnection;
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
    PermissionWhereInput where, int page, int pageSize, Sort orderBy) {
    where = ObjectUtil.defaultValue(where, new PermissionWhereInput());
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    return Kit.connection(
        permissionService.findPage(pageable, where.toFilter()), PermissionConnection.class);
  }

  /** 查询权限 */
  public List<PermissionStatement> permissions(PermissionWhereInput where, Sort orderBy) {
    where = ObjectUtil.defaultValue(where, new PermissionWhereInput());
    return permissionService.findAll(where.toFilter(), orderBy);
  }
}
