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

import cn.asany.nuwa.app.domain.ApplicationMenu;
import cn.asany.ui.resources.service.ComponentService;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.Optional;
import org.hibernate.proxy.HibernateProxy;
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
    if (menu.getComponent() instanceof HibernateProxy) {
      return this.componentService.findById(menu.getComponent().getId());
    }
    return Optional.of(menu.getComponent());
  }
}
