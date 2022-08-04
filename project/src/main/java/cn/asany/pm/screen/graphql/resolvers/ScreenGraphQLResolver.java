package cn.asany.pm.screen.graphql.resolvers;

import cn.asany.pm.field.bean.Field;
import cn.asany.pm.screen.bean.IssueScreen;
import cn.asany.pm.screen.service.FieldToScreenService;
import graphql.kickstart.tools.GraphQLResolver;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 流程界面
 *
 * @author limaofeng
 * @date 2022/7/28 9:12 9:12
 */
@Component
public class ScreenGraphQLResolver implements GraphQLResolver<IssueScreen> {

  /** 字段与页面的中间表 */
  @Autowired private FieldToScreenService fieldToScreenService;

  public List<Field> fields(IssueScreen screen) {

    if (screen.getId() == null) {
      return null;
    }
    /** 根据页面id，查询该页面拥有的字段 */
    return fieldToScreenService.getIssueFields(screen.getId());
  }
}
