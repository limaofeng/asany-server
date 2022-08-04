package cn.asany.workflow.screen.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import java.util.List;
import cn.asany.pm.field.bean.IssueField;
import cn.asany.pm.screen.bean.IssueScreen;
import cn.asany.pm.screen.service.FieldToScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
@Component
public class IssueScreenGraphQLResolver implements GraphQLResolver<IssueScreen> {

  /** 字段与页面的中间表 */
  @Autowired private FieldToScreenService fieldToScreenService;

  public List<IssueField> fields(IssueScreen screen) {

    if (screen.getId() == null) {
      return null;
    }
    /** 根据页面id，查询该页面拥有的字段 */
    return fieldToScreenService.getIssueFields(screen.getId());
  }
}
