/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.nuwa.app.graphql.resolver;

import cn.asany.nuwa.app.domain.ApplicationRoute;
import cn.asany.ui.resources.service.ComponentService;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.Optional;
import org.hibernate.proxy.HibernateProxy;
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
    if (route.getComponent() instanceof HibernateProxy) {
      return this.componentService.findById(route.getComponent().getId());
    }
    return Optional.of(route.getComponent());
  }

  public Optional<cn.asany.ui.resources.domain.Component> breadcrumb(ApplicationRoute route) {
    if (route.getBreadcrumb() == null) {
      return Optional.empty();
    }
    return this.componentService.findById(route.getBreadcrumb().getId());
  }
}
