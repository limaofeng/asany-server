package cn.asany.security.core.graphql;

import cn.asany.security.core.convert.RoleConverter;
import cn.asany.security.core.domain.Role;
import cn.asany.security.core.graphql.input.RoleCreateInput;
import cn.asany.security.core.graphql.input.RoleUpdateInput;
import cn.asany.security.core.graphql.input.RoleWhereInput;
import cn.asany.security.core.graphql.types.RoleConnection;
import cn.asany.security.core.service.RoleService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import net.asany.jfantasy.graphql.util.Kit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * 角色 GraphQL Root Resolver
 *
 * @author limaofeng
 */
@Component
public class RoleGraphQLRootResolver implements GraphQLMutationResolver, GraphQLQueryResolver {

  private final RoleService roleService;
  private final RoleConverter roleConverter;

  public RoleGraphQLRootResolver(
      RoleService roleService, @Autowired(required = false) RoleConverter roleConverter) {
    this.roleService = roleService;
    this.roleConverter = roleConverter;
  }

  /**
   * 查询角色
   *
   * @param where 查询条件
   * @param page 页码
   * @param pageSize 页大小
   * @param orderBy 排序
   * @return 角色连接
   */
  public RoleConnection roles(RoleWhereInput where, int page, int pageSize, Sort orderBy) {
    Pageable pageable = PageRequest.of(page - 1, pageSize, orderBy);
    return Kit.connection(roleService.findPage(pageable, where.toFilter()), RoleConnection.class);
  }

  public Optional<Role> role(Long id) {
    return roleService.findById(id);
  }

  public Role createRole(RoleCreateInput input) {
    return roleService.save(roleConverter.toRole(input));
  }

  public Role updateRole(Long id, RoleUpdateInput input, Boolean merge) {
    return roleService.update(id, roleConverter.toRole(input), merge);
  }

  public Boolean deleteRole(Long id) {
    this.roleService.delete(id);
    return Boolean.TRUE;
  }

  public List<String> addPermissionsToRole(String groupName, String... permissions) {
    return null;
  }
}
