package cn.asany.security.core.graphql.resolvers;

import graphql.kickstart.tools.GraphQLResolver;
import cn.asany.security.core.bean.Role;
import cn.asany.security.core.bean.RoleScope;
import cn.asany.security.core.service.RoleService;
import org.jfantasy.framework.util.common.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @author limaofeng
 * @version V1.0
 * @Description: TODO
 * @date 2019-07-26 14:43
 */
@Component
public class RoleScopeGraphQLResolver implements GraphQLResolver<RoleScope> {
    @Autowired
    private RoleService roleService;

    /**
     * 查询所有角色
     *
     * @param roleScope
     * @param organization
     * @return
     */
    public List<Role> roles(RoleScope roleScope, String organization) {
        if (StringUtil.isBlank(organization)) {
            roleService.getAll(roleScope);
        }
        return new ArrayList<>();//roleService.getAllByOrg(organization,roleScope);
    }

}
