package cn.asany.workflow.field.graphql;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import cn.asany.pm.field.service.IssueFieldConfigurationServce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 字段配置
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Component
public class IssueFieldConfigurationGraphQLMutationResolver implements GraphQLMutationResolver {

  @Autowired private IssueFieldConfigurationServce fieldConfigurationServce;
}
