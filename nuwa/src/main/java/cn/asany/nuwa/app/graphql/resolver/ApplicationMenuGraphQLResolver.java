package cn.asany.nuwa.app.graphql.resolver;

import cn.asany.nuwa.app.domain.ApplicationMenu;
import cn.asany.ui.resources.service.ComponentService;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ApplicationMenuGraphQLResolver implements GraphQLResolver<ApplicationMenu> {

  private final ComponentService componentService;

  public ApplicationMenuGraphQLResolver(ComponentService componentService) {
    this.componentService = componentService;
  }

  public Optional<cn.asany.ui.resources.domain.Component> component(ApplicationMenu menu) {
    if (menu.getComponent() == null) {
      return Optional.empty();
    }
    return this.componentService.findById(menu.getComponent().getId());
  }
}
