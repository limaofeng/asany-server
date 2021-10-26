package cn.asany.workflow.field.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import java.util.List;
import net.whir.hos.issue.field.bean.IssueFieldConfiguration;
import net.whir.hos.issue.field.bean.IssueFieldConfigurationScheme;
import net.whir.hos.issue.field.service.IssueFieldConfigurationServce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @Author: fengmeng @Date: 2019/5/13 18:25 */
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
