package cn.asany.nuwa.app.graphql;

import cn.asany.nuwa.app.converter.ApplicationRouteConverter;
import cn.asany.nuwa.app.domain.ApplicationRoute;
import cn.asany.nuwa.app.graphql.input.ApplicationRouteCreateInput;
import cn.asany.nuwa.app.graphql.input.ApplicationRouteUpdateInput;
import cn.asany.nuwa.app.service.ApplicationRouteService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ApplicationRouteGraphQLRootResolver
    implements GraphQLQueryResolver, GraphQLMutationResolver {

  private final ApplicationRouteService routeService;
  private final ApplicationRouteConverter routeConverter;

  public ApplicationRouteGraphQLRootResolver(
      ApplicationRouteService routeService, ApplicationRouteConverter routeConverter) {
    this.routeService = routeService;
    this.routeConverter = routeConverter;
  }

  public Optional<ApplicationRoute> route(Long id) {
    return routeService.getRoute(id);
  }

  public ApplicationRoute createRoute(ApplicationRouteCreateInput input) {
    return this.routeService.create(routeConverter.toRoute(input));
  }

  public ApplicationRoute updateRoute(Long id, ApplicationRouteUpdateInput input, Boolean merge) {
    return this.routeService.update(id, routeConverter.toRoute(input), merge);
  }

  public ApplicationRoute moveRoute(Long id, Long parentRoute, int location) {
    return this.routeService.update(
        id,
        ApplicationRoute.builder()
            .index(location)
            .parent(ApplicationRoute.builder().id(parentRoute).build())
            .build(),
        true);
  }

  public Boolean deleteRoute(Long id) {
    this.routeService.delete(id);
    return Boolean.TRUE;
  }
}
