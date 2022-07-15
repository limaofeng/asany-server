package cn.asany.nuwa.app.graphql.resolver;

import cn.asany.nuwa.app.domain.ApplicationRoute;
import cn.asany.ui.resources.service.ComponentService;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRouteGraphQLResolver implements GraphQLResolver<ApplicationRoute> {

  private final ComponentService componentService;

  public ApplicationRouteGraphQLResolver(ComponentService componentService) {
    this.componentService = componentService;
  }

  public Optional<cn.asany.ui.resources.domain.Component> component(ApplicationRoute route) {
    if (route.getComponent() == null) {
      return Optional.empty();
    }
    return this.componentService.findById(route.getComponent().getId());
  }

  public Optional<cn.asany.ui.resources.domain.Component> breadcrumb(ApplicationRoute route) {
    if (route.getBreadcrumb() == null) {
      return Optional.empty();
    }
    return this.componentService.findById(route.getBreadcrumb().getId());
  }
}
