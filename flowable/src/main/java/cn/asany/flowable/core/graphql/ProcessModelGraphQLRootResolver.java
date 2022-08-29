package cn.asany.flowable.core.graphql;

import cn.asany.flowable.core.domain.ProcessModel;
import cn.asany.flowable.core.graphql.input.ProcessModelFilter;
import cn.asany.flowable.core.graphql.type.ProcessModelConnection;
import cn.asany.flowable.core.service.ProcessModelService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.jfantasy.framework.dao.OrderBy;
import org.jfantasy.framework.dao.Page;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.jfantasy.graphql.context.AuthorizationGraphQLServletContext;
import org.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;

@Slf4j
@Component
public class ProcessModelGraphQLRootResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final ProcessModelService processModelService;

    public ProcessModelGraphQLRootResolver(ProcessModelService processModelService) {
        this.processModelService = processModelService;
    }

    public ProcessModel processModel(String id) {
        return this.processModelService.get(id);
    }

    public ProcessModelConnection processModels(ProcessModelFilter filter, int currentPage, int pageSize, Sort orderBy) {
        if ("self".equals(filter.getUser())) {
            LoginUser loginUser = SpringSecurityUtils.getCurrentUser();
            filter.setUser(loginUser.getUid().toString());
        }
        Page<ProcessModel> page = processModelService.findPage(Page.of(currentPage, pageSize, OrderBy.sort(orderBy)), filter);
        return Kit.connection(page, ProcessModelConnection.class);
    }

    public ProcessModel importProcessModel(Part file, DataFetchingEnvironment environment) throws IOException {
        AuthorizationGraphQLServletContext context = environment.getContext();
        HttpServletRequest request = context.getRequest();
        LoginUser loginUser = SpringSecurityUtils.getCurrentUser();
        return this.processModelService.importProcessModel(loginUser, request, file);
    }

    public Boolean deleteProcessModel(String id) {
        this.processModelService.delete(id);
        return Boolean.TRUE;
    }
}
