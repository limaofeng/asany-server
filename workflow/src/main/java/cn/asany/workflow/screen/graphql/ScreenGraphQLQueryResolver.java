package cn.asany.workflow.screen.graphql;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import java.util.List;
import net.whir.hos.issue.screen.bean.IssueScreen;
import net.whir.hos.issue.screen.service.IssueScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author penghanying @ClassName: ScreenGraphQLQueryResolver @Description: (这里用一句话描述这个类的作用)
 * @date 2019/5/23
 */
@Component
public class ScreenGraphQLQueryResolver implements GraphQLQueryResolver {

  /** 页面的service */
  @Autowired private IssueScreenService issueScreenService;

  /**
   * @ClassName: IssueTaskGraphQLQueryResolver @Description: 查询全部页面
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public List<IssueScreen> screens() {
    return issueScreenService.screens();
  }

  /**
   * @ClassName: IssueTaskGraphQLQueryResolver @Description: 查询某个页面分配的全部字段
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public IssueScreen screen(Long id) {
    return issueScreenService.screen(id);
  }
}
