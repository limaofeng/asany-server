package cn.asany.security.core.graphql.resolver;

import cn.asany.security.core.domain.Role;
import cn.asany.security.core.domain.RoleSpace;
import cn.asany.security.core.service.RoleService;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.ArrayList;
import java.util.List;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
@Component
public class RoleScopeGraphQLResolver implements GraphQLResolver<RoleSpace> {
  @Autowired private RoleService roleService;

  /**
   * 查询所有角色
   *
   * @param roleScope
   * @param organization
   * @return
   */
  public List<Role> roles(RoleSpace roleScope, String organization) {
    if (StringUtil.isBlank(organization)) {
      //      roleService.getAll(roleScope);
    }
    return new ArrayList<>(); // roleService.getAllByOrg(organization,roleScope);
  }
}
