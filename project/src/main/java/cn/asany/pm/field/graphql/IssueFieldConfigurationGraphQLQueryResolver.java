package cn.asany.pm.field.graphql;

import cn.asany.pm.field.bean.FieldConfiguration;
import cn.asany.pm.field.bean.FieldConfigurationScheme;
import cn.asany.pm.field.service.FieldConfigurationService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Component
public class IssueFieldConfigurationGraphQLQueryResolver implements GraphQLQueryResolver {

  @Autowired private FieldConfigurationService fieldConfigurationService;

  public List<FieldConfiguration> issueFieldConfigurations() {
    return fieldConfigurationService.getFieldConfigurations();
  }

  public FieldConfiguration issueFieldConfiguration(Long id) {
    return fieldConfigurationService.getFieldConfiguration(id);
  }

  public List<FieldConfigurationScheme> issueFieldConfigurationSchemes() {
    return fieldConfigurationService.getFieldConfigurationSchemes();
  }

  public FieldConfigurationScheme issueFieldConfigurationScheme(Long id) {
    return fieldConfigurationService.getFieldConfigurationScheme(id);
  }
}
