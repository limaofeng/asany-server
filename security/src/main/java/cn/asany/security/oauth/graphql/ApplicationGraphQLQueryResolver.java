package cn.asany.security.oauth.graphql;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ApplicationGraphQLQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private AppService appService;

    public ApplicationConnection applicationConnection(ApplicationFilter filter, int page, int pageSize, OrderBy orderBy) {
        Pager<Application> pager = new Pager<>(page, pageSize, orderBy);
        filter = ObjectUtil.defaultValue(filter, new ApplicationFilter());
        return Kit.connection(appService.findPager(pager, filter.build()), ApplicationConnection.class);
    }

    public List<Application> applications(ApplicationFilter filter) {
        return appService.findAll(filter.build());
    }

    public Optional<Application> application(String id) {
        return appService.getApplication(id);
    }

}
