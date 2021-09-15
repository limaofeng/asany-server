package cn.asany.nuwa.app.graphql;

import cn.asany.nuwa.app.bean.Application;
import cn.asany.nuwa.app.bean.ApplicationRoute;
import cn.asany.nuwa.app.converter.ApplicationConverter;
import cn.asany.nuwa.app.graphql.input.ApplicationCreateInput;
import cn.asany.nuwa.app.graphql.input.RouteCreateInput;
import cn.asany.nuwa.app.graphql.input.RouteUpdateInput;
import cn.asany.nuwa.app.service.ApplicationService;
import cn.asany.nuwa.app.service.dto.NativeApplication;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.stereotype.Component;

/**
 * 应用 Mutation
 *
 * @author limaofeng
 */
@Component
public class ApplicationGraphQLMutationResolver implements GraphQLMutationResolver {

  private final ApplicationService applicationService;
  public final ApplicationConverter applicationConverter;

  public ApplicationGraphQLMutationResolver(
      ApplicationService applicationService, ApplicationConverter applicationConverter) {
    this.applicationService = applicationService;
    this.applicationConverter = applicationConverter;
  }

  public Application createApplication(ApplicationCreateInput input) {
    NativeApplication application = applicationConverter.toNativeApplication(input);
    return applicationService.createApplication(application);
  }

  public Application updateApplication(Long id, ApplicationCreateInput input, Boolean merge) {
    return new Application();
  }

  public Boolean deleteApplication(Long id) {
    this.applicationService.deleteApplication(id);
    return Boolean.TRUE;
  }

  public ApplicationRoute createRoute(RouteCreateInput input) {
    return null;
  }

  public ApplicationRoute updateRoute(Long id, RouteUpdateInput input, Boolean merge) {
    return null;
  }

  public ApplicationRoute deleteRoute(Long id) {
    return null;
  }

  public ApplicationRoute moveRoute(Long id, Long parentRoute, int location) {
    return null;
  }
}
