package cn.asany.shanhai.demo.graphql;

import cn.asany.shanhai.demo.bean.User;
import cn.asany.shanhai.demo.service.UserService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserGraphQLMutationResolver implements GraphQLMutationResolver {

    @Autowired
    private UserService userService;

    public User createUser(User user) {
        return userService.save(user);
    }

}
