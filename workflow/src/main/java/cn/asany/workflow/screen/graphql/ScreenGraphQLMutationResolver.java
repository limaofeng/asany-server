package cn.asany.workflow.screen.graphql;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import cn.asany.pm.screen.bean.IssueScreen;
import cn.asany.pm.screen.bean.IssueScreenTabPane;
import cn.asany.pm.screen.service.FieldToScreenService;
import cn.asany.pm.screen.service.IssueScreenService;
import cn.asany.pm.screen.service.IssueScreenTabPaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author limaofeng@msn.com @ClassName: ScreenGraphQLMutationResolver @Description: (这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
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
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public IssueScreen createIssueScreen(IssueScreen issueScreen) {
    return issueScreenService.createIssueScreen(issueScreen);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 修改页面
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public IssueScreen updateIssueScreen(Long id, Boolean merge, IssueScreen issueScreen) {
    return issueScreenService.updateIssueScreen(id, merge, issueScreen);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 删除页面
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Boolean removeIssueScreen(Long id) {
    return issueScreenService.removeIssueScreen(id);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 为页面添加 TabPane
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public IssueScreenTabPane createIssueScreenTabPane(Long screenId, String name) {
    return issueScreenTabPaneService.createIssueScreenTabPane(screenId, name);
  }

  /**
   * @ClassName: ScreenGraphQLMutationResolver @Description: 删除TabPane
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Boolean removeIssueScreenTabPane(Long id) {
    return issueScreenTabPaneService.removeIssueScreenTabPane(id);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 将字段添加到界面
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Boolean createIssueScreenField(Long screen, Long field) {
    return fieldToScreenService.createIssueScreenField(screen, field);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 将字段添加到界面中的TabPane
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Boolean createIssueScreenTabPaneField(Long screen, Long tabPane, Long field) {
    return fieldToScreenService.createIssueScreenTabPaneField(screen, tabPane, field);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 删除已分配到界面的字段
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Boolean removeIssueScreenField(Long screen, Long field) {
    return fieldToScreenService.removeIssueScreenField(screen, field);
  }

  /**
   * @ClassName: IussTaskGraphQLMutationResolver @Description: 删除已分配到TabPane的字段
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Boolean removeIssueScreenTabPaneField(Long screen, Long tabPane, Long field) {
    return fieldToScreenService.removeIssueScreenTabPaneField(screen, tabPane, field);
  }
}
