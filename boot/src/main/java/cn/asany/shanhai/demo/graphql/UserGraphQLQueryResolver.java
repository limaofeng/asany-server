package cn.asany.shanhai.demo.graphql;

import cn.asany.shanhai.demo.bean.User;
import cn.asany.shanhai.demo.graphql.inputs.UserFilter;
import cn.asany.shanhai.demo.graphql.types.UserConnection;
import cn.asany.shanhai.demo.service.UserService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author limaofeng
 */
@Component
public class UserGraphQLQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private UserService userService;

    public UserConnection users(UserFilter filter, int page, int pageSize, OrderBy orderBy) {
        Pager<User> pager = new Pager<>(page, pageSize, orderBy);
        filter = ObjectUtil.defaultValue(filter, new UserFilter());
        return Kit.connection(userService.findPager(pager, filter.build()), UserConnection.class);
    }

    public Optional<User> user(Long id) {
        return userService.get(id);
    }

}
