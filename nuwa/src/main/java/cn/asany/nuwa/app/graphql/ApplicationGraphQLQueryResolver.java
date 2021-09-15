package cn.asany.nuwa.app.graphql;

import cn.asany.nuwa.app.bean.Application;
import cn.asany.nuwa.app.bean.ApplicationRoute;
import cn.asany.nuwa.app.graphql.input.ApplicationFilter;
import cn.asany.nuwa.app.graphql.type.ApplicationIdType;
import cn.asany.nuwa.app.service.ApplicationService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import java.util.Optional;
import org.jfantasy.framework.util.common.ObjectUtil;
import org.jfantasy.framework.util.regexp.RegexpConstant;
import org.jfantasy.framework.util.regexp.RegexpUtil;
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

  public Optional<Application> application(String id, ApplicationIdType idType, String space) {
    idType = ObjectUtil.defaultValue(idType, () -> getDefaultApplicationIdType(id));
    if (ApplicationIdType.CLIENT_ID.equals(idType)) {
      return applicationService.findDetailsByClientId(id);
    }
    return applicationService.findDetailsById(Long.valueOf(id));
  }

  public List<Application> applications(ApplicationFilter filter) {
    return applicationService.findAll(filter.build());
  }

  public Optional<ApplicationRoute> route(Long id) {
    return applicationService.getRoute(id);
  }
}
