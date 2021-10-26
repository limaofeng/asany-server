package cn.asany.workflow.field.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import net.whir.hos.issue.field.bean.IssueFieldConfigurationItem;
import net.whir.hos.issue.field.bean.enums.FieldType;
import net.whir.hos.issue.field.service.IssueFieldConfigurationServce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author penghanying @ClassName: IssueFieldConfigurationItemGraphQLResolver @Description:
 *     (这里用一句话描述这个类的作用)
 * @date 2019/6/26
 */
@Component
public class IssueFieldConfigurationItemGraphQLResolver
    implements GraphQLResolver<IssueFieldConfigurationItem> {

  @Autowired private IssueFieldConfigurationServce configurationServce;

  /** 名称 */
  public String name(IssueFieldConfigurationItem item) {
    if (item.getField() != null) {
      return item.getField().getName();
    }
    return null;
  }

  /** label */
  public String label(IssueFieldConfigurationItem item) {
    if (item.getField() != null) {
      return item.getField().getLabel();
    }
    return null;
  }

  /** 字段类型 */
  public FieldType type(IssueFieldConfigurationItem item) {
    if (item.getField() != null) {
      return item.getField().getType();
    }
    return null;
  }

  /** 排序 */
  public Integer order(IssueFieldConfigurationItem item) {
    if (item.getField() == null) {
      return null;
    }
    return configurationServce.order(item.getId());
  }
}
