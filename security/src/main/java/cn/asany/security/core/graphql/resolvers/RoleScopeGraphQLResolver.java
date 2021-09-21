package cn.asany.security.core.graphql.resolvers;

import cn.asany.security.core.bean.Role;
import cn.asany.security.core.bean.RoleSpace;
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
 * @date 2019-07-26 14:43
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
