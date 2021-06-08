package cn.asany.security.oauth.graphql;

import cn.asany.security.oauth.bean.OAuthApplication;
import cn.asany.security.oauth.graphql.input.ApplicationFilter;
import cn.asany.security.oauth.graphql.type.ApplicationConnection;
import cn.asany.security.oauth.service.OAuthApplicationService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.Pager;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.graphql.util.Kit;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class ApplicationGraphQLQueryResolver implements GraphQLQueryResolver {

    @Autowired
    private OAuthApplicationService OAuthApplicationService;

    public ApplicationConnection applications(ApplicationFilter filter, int page, int pageSize, OrderBy orderBy) {
        Pager<OAuthApplication> pager = new Pager<>(page, pageSize, orderBy);
        filter = ObjectUtil.defaultValue(filter, new ApplicationFilter());
        return Kit.connection(OAuthApplicationService.findPager(pager, filter.build()), ApplicationConnection.class);
    }

    public Optional<OAuthApplication> application(String id) {
        return OAuthApplicationService.getApplication(id);
    }

}
