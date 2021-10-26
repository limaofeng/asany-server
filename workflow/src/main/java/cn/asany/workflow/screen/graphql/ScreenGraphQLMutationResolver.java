package cn.asany.workflow.screen.graphql;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import net.whir.hos.issue.screen.bean.IssueScreen;
import net.whir.hos.issue.screen.bean.IssueScreenTabPane;
import net.whir.hos.issue.screen.service.FieldToScreenService;
import net.whir.hos.issue.screen.service.IssueScreenService;
import net.whir.hos.issue.screen.service.IssueScreenTabPaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author penghanying @ClassName: ScreenGraphQLMutationResolver @Description: (这里用一句话描述这个类的作用)
 * @date 2019/5/23
 */
@Component
public class ScreenGraphQLMutationResolver implements GraphQLMutationResolver {
  // 页面的service
  @Autowired private IssueScreenService issueScreenService;

  // 页面的tabPane的Service
  @Autowired private IssueScreenTabPaneService issueScreenTabPaneService;

  // 字段页面的Service
  @Autowired private FieldToScreenService fieldToScreenService;

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 添加页面
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public IssueScreen createIssueScreen(IssueScreen issueScreen) {
    return issueScreenService.createIssueScreen(issueScreen);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 修改页面
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public IssueScreen updateIssueScreen(Long id, Boolean merge, IssueScreen issueScreen) {
    return issueScreenService.updateIssueScreen(id, merge, issueScreen);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 删除页面
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public Boolean removeIssueScreen(Long id) {
    return issueScreenService.removeIssueScreen(id);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 为页面添加 TabPane
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public IssueScreenTabPane createIssueScreenTabPane(Long screenId, String name) {
    return issueScreenTabPaneService.createIssueScreenTabPane(screenId, name);
  }

  /**
   * @ClassName: ScreenGraphQLMutationResolver @Description: 删除TabPane
   *
   * @author penghanying
   * @date 2019/5/24
   */
  public Boolean removeIssueScreenTabPane(Long id) {
    return issueScreenTabPaneService.removeIssueScreenTabPane(id);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 将字段添加到界面
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public Boolean createIssueScreenField(Long screen, Long field) {
    return fieldToScreenService.createIssueScreenField(screen, field);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 将字段添加到界面中的TabPane
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public Boolean createIssueScreenTabPaneField(Long screen, Long tabPane, Long field) {
    return fieldToScreenService.createIssueScreenTabPaneField(screen, tabPane, field);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 删除已分配到界面的字段
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public Boolean removeIssueScreenField(Long screen, Long field) {
    return fieldToScreenService.removeIssueScreenField(screen, field);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 删除已分配到TabPane的字段
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public Boolean removeIssueScreenTabPaneField(Long screen, Long tabPane, Long field) {
    return fieldToScreenService.removeIssueScreenTabPaneField(screen, tabPane, field);
  }
}
