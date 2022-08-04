package cn.asany.pm.field.graphql;

import cn.asany.pm.field.service.FieldConfigurationService;
import graphql.kickstart.tools.GraphQLMutationResolver;
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

  @Autowired private FieldConfigurationService fieldConfigurationService;
}
