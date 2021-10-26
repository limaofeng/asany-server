package cn.asany.workflow.field.graphql;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import net.whir.hos.issue.field.service.IssueFieldConfigurationServce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/** @Author: fengmeng @Date: 2019/5/13 18:25 */
@Component
public class IssueFieldConfigurationGraphQLMutationResolver implements GraphQLMutationResolver {

  @Autowired private IssueFieldConfigurationServce fieldConfigurationServce;
}
