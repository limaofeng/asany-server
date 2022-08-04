package cn.asany.workflow.screen.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import java.util.List;
import cn.asany.pm.screen.bean.IssueScreen;
import cn.asany.pm.screen.service.IssueScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng@msn.com @ClassName: ScreenGraphQLQueryResolver @Description: (这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
@Component
public class ScreenGraphQLQueryResolver implements GraphQLQueryResolver {

  /** 页面的service */
  @Autowired private IssueScreenService issueScreenService;

  /**
   * @ClassName: IssueTaskGraphQLQueryResolver @Description: 查询全部页面
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public List<IssueScreen> screens() {
    return issueScreenService.screens();
  }

  /**
   * @ClassName: IssueTaskGraphQLQueryResolver @Description: 查询某个页面分配的全部字段
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public IssueScreen screen(Long id) {
    return issueScreenService.screen(id);
  }
}
