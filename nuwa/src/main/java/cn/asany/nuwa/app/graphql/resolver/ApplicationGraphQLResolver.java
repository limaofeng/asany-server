package cn.asany.nuwa.app.graphql.resolver;

import cn.asany.nuwa.app.domain.Application;
import cn.asany.nuwa.app.domain.ApplicationRoute;
import cn.asany.nuwa.app.domain.ClientSecret;
import cn.asany.nuwa.app.domain.Licence;
import cn.asany.nuwa.app.graphql.input.ApplicationRouteFilter;
import cn.asany.nuwa.app.service.ApplicationService;
import cn.asany.nuwa.app.service.LicenceService;
import graphql.execution.ExecutionStepInfo;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jfantasy.framework.security.LoginUser;
import org.jfantasy.framework.security.SpringSecurityUtils;
import org.springframework.stereotype.Component;

/** @author limaofeng */
@Component
public class ApplicationGraphQLResolver implements GraphQLResolver<Application> {

  private final LicenceService licenceService;
  private final ApplicationService applicationService;

  public ApplicationGraphQLResolver(
      ApplicationService applicationService, LicenceService licenceService) {
    this.applicationService = applicationService;
    this.licenceService = licenceService;
  }

  public Optional<ApplicationRoute> layoutRoute(
      Application application, String space, DataFetchingEnvironment environment) {
    List<ApplicationRoute> routes =
        this.routes(
            application, ApplicationRouteFilter.builder().space(space).build(), environment);
    return routes.stream()
        .filter(item -> "/".equals(item.getPath()) && item.getLevel() == 1)
        .findAny();
  }

  public Optional<ApplicationRoute> loginRoute(
      Application application, String space, DataFetchingEnvironment environment) {
    List<ApplicationRoute> routes =
        this.routes(
            application, ApplicationRouteFilter.builder().space(space).build(), environment);
    return routes.stream()
        .filter(item -> "/login".equals(item.getPath()) && item.getLevel() == 1)
        .findAny();
  }

  public List<ApplicationRoute> routes(
      Application application, ApplicationRouteFilter filter, DataFetchingEnvironment environment) {
    ExecutionStepInfo parent = environment.getExecutionStepInfo().getParent();
    String space = filter.getSpace();
    if ("application".equals(parent.getFieldDefinition().getName())) {
      Stream<ApplicationRoute> stream =
          application.getRoutes().stream().filter(item -> space.equals(item.getSpace().getId()));

      if (filter.getEnabled() != null) {
        stream = stream.filter(item -> filter.getEnabled().equals(item.getEnabled()));
      }

      return stream.collect(Collectors.toList());
    }
    List<ApplicationRoute> routes =
        this.applicationService.findRouteAllByApplicationAndSpaceWithComponent(
            application.getId(), space);

    if (filter.getEnabled() != null) {
      return routes.stream()
          .filter(item -> filter.getEnabled().equals(item.getEnabled()))
          .collect(Collectors.toList());
    }

    return routes;
  }

  public Boolean dingtalkIntegration(Application application) {
    return Boolean.FALSE;
  }

  public Optional<Licence> licence(Application application) {
    LoginUser user = SpringSecurityUtils.getCurrentUser();
    if (user == null) {
      return Optional.empty();
    }
    Long orgId = user.getAttribute("organization");
    if (orgId == null) {
      return Optional.empty();
    }
    return this.licenceService.findOneByActive(application.getId(), orgId);
  }

  public List<Licence> licences(Application application) {
    return application.getLicences();
  }

  public List<ClientSecret> clientSecrets(Application application) {
    return application.getClientSecretsAlias();
  }
}
