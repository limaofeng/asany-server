package cn.asany.workflow.field.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import java.util.List;
import cn.asany.pm.field.bean.IssueFieldConfiguration;
import cn.asany.pm.field.bean.IssueFieldConfigurationScheme;
import cn.asany.pm.field.service.IssueFieldConfigurationServce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @author limaofeng@msn.com @date 2022/7/28 9:12 9:12
@Component
public class IssueFieldConfigurationGraphQLQueryResolver implements GraphQLQueryResolver {

  @Autowired private IssueFieldConfigurationServce fieldConfigurationServce;

  public List<IssueFieldConfiguration> issueFieldConfigurations() {
    return fieldConfigurationServce.getFieldConfigurations();
  }

  public IssueFieldConfiguration issueFieldConfiguration(Long id) {
    return fieldConfigurationServce.getFieldConfiguration(id);
  }

  public List<IssueFieldConfigurationScheme> issueFieldConfigurationSchemes() {
    return fieldConfigurationServce.getFieldConfigurationSchemes();
  }

  public IssueFieldConfigurationScheme issueFieldConfigurationScheme(Long id) {
    return fieldConfigurationServce.getFieldConfigurationScheme(id);
  }
}
