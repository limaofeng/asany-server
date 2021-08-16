package cn.asany.nuwa.app.graphql;

import cn.asany.nuwa.app.bean.Application;
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

  public Optional<Application> application(String id, ApplicationIdType idType, String space) {
    idType =
        ObjectUtil.defaultValue(
            idType,
            () ->
                RegexpUtil.isMatch(id, RegexpConstant.VALIDATOR_INTEGE)
                    ? ApplicationIdType.ID
                    : ApplicationIdType.CLIENT_ID);
    if (ApplicationIdType.CLIENT_ID.equals(idType)) {
      return applicationService.findByClientIdWithRoute(id, space);
    }
    return applicationService.findByIdWithRoute(Long.valueOf(id), space);
  }

  public List<Application> applications(ApplicationFilter filter) {
    return applicationService.findAll(filter.build());
  }
}
