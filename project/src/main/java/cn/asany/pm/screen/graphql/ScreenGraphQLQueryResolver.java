package cn.asany.pm.screen.graphql;

import cn.asany.pm.screen.bean.IssueScreen;
import cn.asany.pm.screen.service.IssueScreenService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * 界面接口
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
@Component
public class ScreenGraphQLQueryResolver implements GraphQLQueryResolver {

  /** 页面的service */
  private final IssueScreenService issueScreenService;

  public ScreenGraphQLQueryResolver(IssueScreenService issueScreenService) {
    this.issueScreenService = issueScreenService;
  }

  /**
   * 查询全部页面
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public List<IssueScreen> screens() {
    return issueScreenService.screens();
  }

  /**
   * 查询某个页面分配的全部字段
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public IssueScreen screen(Long id) {
    return issueScreenService.screen(id);
  }
}
