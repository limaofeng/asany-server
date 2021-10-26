package cn.asany.workflow.screen.graphql.resolvers;

import com.coxautodev.graphql.tools.GraphQLResolver;
import java.util.List;
import net.whir.hos.issue.field.bean.IssueFieldConfigurationItem;
import net.whir.hos.issue.screen.bean.IssueScreenTabPane;
import net.whir.hos.issue.screen.service.FieldToScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2019-05-23 21:50
 */
@Component
public class IssueScreenTabPaneGraphQLResolver implements GraphQLResolver<IssueScreenTabPane> {

  /** 字段与页面的中间表 */
  @Autowired private FieldToScreenService fieldToScreenService;

  //    public List<FieldToScreen> fields(IssueScreenTabPane screen){
  //
  //        if(screen.getId() == null){
  //            return null;
  //        }
  //        /**根据TabPane的id，查询该TabPane拥有的字段*/
  //        return fieldToScreenService.getTabPaneFields(screen.getId());
  //
  //    }

  public List<IssueFieldConfigurationItem> fields(IssueScreenTabPane tabPane) {

    if (tabPane.getId() == null) {
      return null;
    }
    /** 根据TabPane的id，查询该TabPane拥有的字段 */
    return fieldToScreenService.getTabPaneFieldItem(tabPane.getId());
  }
}
