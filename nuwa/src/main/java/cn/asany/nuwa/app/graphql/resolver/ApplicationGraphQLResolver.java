package cn.asany.nuwa.app.graphql.resolver;

import cn.asany.nuwa.app.domain.*;
import cn.asany.nuwa.app.graphql.input.ApplicationRouteFilter;
import cn.asany.nuwa.app.service.ApplicationService;
import cn.asany.nuwa.app.service.LicenceService;
import cn.asany.ui.library.convert.LibraryConverter;
import cn.asany.ui.library.domain.Library;
import cn.asany.ui.library.graphql.type.ComponentLibrary;
import cn.asany.ui.library.service.LibraryService;
import graphql.execution.ExecutionStepInfo;
import graphql.kickstart.tools.GraphQLResolver;
import graphql.schema.DataFetchingEnvironment;
import java.util.Arrays;
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
  private final LibraryService libraryService;
  private final ApplicationService applicationService;
  private final LibraryConverter libraryConverter;

  public ApplicationGraphQLResolver(
      ApplicationService applicationService,
      LibraryService libraryService,
      LicenceService licenceService,
      LibraryConverter libraryConverter) {
    this.applicationService = applicationService;
    this.licenceService = licenceService;
    this.libraryService = libraryService;
    this.libraryConverter = libraryConverter;
  }

  public Optional<ApplicationRoute> layoutRoute(
      Application application, DataFetchingEnvironment environment) {
    List<ApplicationRoute> routes =
        this.routes(application, ApplicationRouteFilter.builder().build(), environment);
    return routes.stream()
        .filter(item -> "/".equals(item.getPath()) && item.getLevel() == 1)
        .findAny();
  }

  public Optional<ApplicationRoute> loginRoute(
      Application application, DataFetchingEnvironment environment) {
    List<ApplicationRoute> routes =
        this.routes(application, ApplicationRouteFilter.builder().build(), environment);
    return routes.stream()
        .filter(
            item ->
                Arrays.asList("/login", "/sign-in").contains(item.getPath())
                    && item.getLevel() == 1)
        .findAny();
  }

  public List<ApplicationRoute> routes(
      Application application, ApplicationRouteFilter filter, DataFetchingEnvironment environment) {
    ExecutionStepInfo parent = environment.getExecutionStepInfo().getParent();
    if ("application".equals(parent.getFieldDefinition().getName())) {
      Stream<ApplicationRoute> stream = application.getRoutes().stream();

      if (filter.getEnabled() != null) {
        stream = stream.filter(item -> filter.getEnabled().equals(item.getEnabled()));
      }

      return stream.collect(Collectors.toList());
    }
    List<ApplicationRoute> routes =
        this.applicationService.findRouteAllByApplicationWithComponent(application.getId());

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

  public List<ApplicationDependency> dependencies(Application application) {
    return this.applicationService.findDependencies(application.getId());
  }

  public Optional<ComponentLibrary> componentLibrary(Application application) {
    List<ApplicationDependency> dependencies = this.dependencies(application);
    Optional<String> libraryId =
        dependencies.stream()
            .filter(item -> item.getName().equals("component.library"))
            .findFirst()
            .map(ApplicationDependency::getValue);
    if (!libraryId.isPresent()) {
      return Optional.empty();
    }
    Optional<Library> library = libraryService.findById(Long.valueOf(libraryId.get()));
    return library.map(this.libraryConverter::toComponentLibrary);
  }

  public List<Licence> licences(Application application) {
    return application.getLicences();
  }

  public List<ClientSecret> clientSecrets(Application application) {
    return application.getClientSecretsAlias();
  }

  public Optional<ApplicationModuleConfiguration> module(Application application, String id) {
    return application.getModules().stream()
        .filter(item -> item.getModule().getId().equals(id))
        .findAny();
  }
}
