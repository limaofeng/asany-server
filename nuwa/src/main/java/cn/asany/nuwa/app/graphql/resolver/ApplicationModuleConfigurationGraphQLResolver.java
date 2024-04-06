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
