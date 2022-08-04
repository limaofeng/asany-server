package cn.asany.pm.field.graphql.resolvers;

import cn.asany.pm.field.bean.FieldConfigurationItem;
import cn.asany.pm.field.bean.FieldType;
import cn.asany.pm.field.service.FieldConfigurationService;
import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;

/**
 * 问题字段配置
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
@Component
public class IssueFieldConfigurationItemGraphQLResolver
    implements GraphQLResolver<FieldConfigurationItem> {

  private final FieldConfigurationService configurationService;

  public IssueFieldConfigurationItemGraphQLResolver(
      FieldConfigurationService configurationService) {
    this.configurationService = configurationService;
  }

  /** 名称 */
  public String name(FieldConfigurationItem item) {
    if (item.getField() != null) {
      return item.getField().getName();
    }
    return null;
  }

  /** label */
  public String label(FieldConfigurationItem item) {
    if (item.getField() != null) {
      return item.getField().getLabel();
    }
    return null;
  }

  /** 字段类型 */
  public FieldType type(FieldConfigurationItem item) {
    if (item.getField() != null) {
      return item.getField().getType();
    }
    return null;
  }

  /** 排序 */
  public Integer order(FieldConfigurationItem item) {
    if (item.getField() == null) {
      return null;
    }
    return configurationService.order(item.getId());
  }
}
