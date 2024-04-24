package cn.asany.flowable.core.graphql;

import cn.asany.flowable.core.domain.ProcessModel;
import cn.asany.flowable.core.graphql.input.ProcessModelWhereInput;
import cn.asany.flowable.core.graphql.type.ProcessModelConnection;
import cn.asany.flowable.core.service.ProcessModelService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import net.asany.jfantasy.framework.dao.OrderBy;
import net.asany.jfantasy.framework.dao.Page;
import net.asany.jfantasy.framework.dao.jpa.PropertyFilter;
import net.asany.jfantasy.framework.security.LoginUser;
import net.asany.jfantasy.framework.security.SpringSecurityUtils;
import net.asany.jfantasy.graphql.context.AuthorizationGraphQLServletContext;
import net.asany.jfantasy.graphql.util.Kit;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * 工作流/流程模型
 *
 * @author limaofeng
 */
@Slf4j
@Component
public class ProcessModelGraphQLRootResolver
    implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final ProcessModelService processModelService;

  public ProcessModelGraphQLRootResolver(ProcessModelService processModelService) {
    this.processModelService = processModelService;
  }

  public ProcessModel processModel(String id) {
    return this.processModelService.get(id);
  }

  public ProcessModelConnection processModels(
      ProcessModelWhereInput where, int currentPage, int pageSize, Sort orderBy) {
    if ("self".equals(where.getUser())) {
      LoginUser loginUser = SpringSecurityUtils.getCurrentUser();
      where.setUser(loginUser.getUid().toString());
    }
    Page<ProcessModel> page =
        processModelService.findPage(
            Page.of(currentPage, pageSize, OrderBy.sort(orderBy)), PropertyFilter.newFilter());
    return Kit.connection(page, ProcessModelConnection.class);
  }

  public ProcessModel importProcessModel(Part file, DataFetchingEnvironment environment)
      throws IOException {
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
