package cn.asany.security.oauth.graphql;

import cn.asany.security.oauth.bean.Application;
import cn.asany.security.oauth.graphql.inputs.ApplicationFilter;
import cn.asany.security.oauth.graphql.types.ApplicationConnection;
import cn.asany.security.oauth.service.ApplicationService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ApplicationGraphQLQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private ApplicationService applicationService;

    public ApplicationConnection applications(ApplicationFilter filter, int page, int pageSize, OrderBy orderBy) {
        Pager<Application> pager = new Pager<>(page, pageSize, orderBy);
        filter = ObjectUtil.defaultValue(filter, new ApplicationFilter());
        return Kit.connection(applicationService.findPager(pager, filter.build()), ApplicationConnection.class);
    }

    public Optional<Application> application(String id) {
        return applicationService.getApplication(id);
    }

}
