package cn.asany.nuwa.app.graphql;

import cn.asany.nuwa.app.domain.Application;
import cn.asany.nuwa.app.graphql.input.ApplicationFilter;
import cn.asany.nuwa.app.graphql.type.ApplicationIdType;
import cn.asany.nuwa.app.service.ApplicationService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.regexp.RegexpConstant;
import org.jfantasy.framework.util.regexp.RegexpUtil;
import org.jfantasy.graphql.util.GraphqlUtil;
import org.springframework.stereotype.Component;

/**
 * 应用
 *
 * @author limaofeng
 */
@Component
public class ApplicationGraphQLQueryResolver implements GraphQLQueryResolver {

  private final ApplicationService applicationService;

  public ApplicationGraphQLQueryResolver(ApplicationService applicationService) {
    this.applicationService = applicationService;
  }

  private static ApplicationIdType getDefaultApplicationIdType(String id) {
    return RegexpUtil.isMatch(id, RegexpConstant.VALIDATOR_INTEGE)
        ? ApplicationIdType.ID
        : ApplicationIdType.CLIENT_ID;
  }

  public Optional<Application> application(
      String id, ApplicationIdType idType, String space, DataFetchingEnvironment environment) {
    idType = ObjectUtil.defaultValue(idType, () -> getDefaultApplicationIdType(id));
    boolean hasFetchRoutes = GraphqlUtil.hasFetchFields(environment, "routes");
    boolean hasFetchMenus = GraphqlUtil.hasFetchFields(environment, "menus");
    if (ApplicationIdType.CLIENT_ID.equals(idType)) {
      return applicationService.findDetailsByClientId(id, hasFetchRoutes, hasFetchMenus);
    }
    return applicationService.findDetailsById(Long.valueOf(id), hasFetchRoutes, hasFetchMenus);
  }

  public List<Application> applications(ApplicationFilter filter) {
    return applicationService.findAll(filter.build());
  }
}
