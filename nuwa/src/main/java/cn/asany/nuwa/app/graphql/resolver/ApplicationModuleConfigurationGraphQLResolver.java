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

import cn.asany.nuwa.app.domain.ApplicationModuleConfiguration;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class ApplicationModuleConfigurationGraphQLResolver
    implements GraphQLResolver<ApplicationModuleConfiguration> {

  public String key(ApplicationModuleConfiguration configuration) {
    return configuration.getModule().getId();
  }

  public String name(ApplicationModuleConfiguration configuration) {
    return configuration.getModule().getName();
  }

  public String description(ApplicationModuleConfiguration configuration) {
    return configuration.getModule().getDescription();
  }

  public Map<String, String> configuration(ApplicationModuleConfiguration configuration) {
    return configuration.getValues();
  }

  public String value(ApplicationModuleConfiguration configuration, String key) {
    return configuration.getValues().get(key);
  }
}
