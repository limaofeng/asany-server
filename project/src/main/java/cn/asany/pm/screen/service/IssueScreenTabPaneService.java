package cn.asany.pm.screen.service;

import cn.asany.pm.screen.bean.FieldToScreen;
import cn.asany.pm.screen.bean.IssueScreen;
import cn.asany.pm.screen.bean.IssueScreenTabPane;
import cn.asany.pm.screen.dao.FieldToScreenDao;
import cn.asany.pm.screen.dao.IssueScreenDao;
import cn.asany.pm.screen.dao.IssueScreenTabPaneDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author limaofeng@msn.com @ClassName: IssueScreenTabPaneService @Description:
 *     页面中添加tabpane(这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IssueScreenTabPaneService {

  @Autowired private IssueScreenTabPaneDao issueScreenTabPaneDao;

  @Autowired private IssueScreenDao issueScreenDao;

  @Autowired private FieldToScreenDao fieldToScreenDao;

  /**
   * @ClassName: IssueScreenTabPaneService @Description: 为页面添加 TabPane
   *
   * @param screenId 页面id
   * @param name 名称
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public IssueScreenTabPane createIssueScreenTabPane(Long screenId, String name) {
    IssueScreenTabPane issueScreenTabPane = new IssueScreenTabPane();
    issueScreenTabPane.setName(name);
    IssueScreen issueScreen = issueScreenDao.findById(screenId).orElse(null);
    if (issueScreen != null) {
      issueScreenTabPane.setIssueScreen(issueScreen);
    }
    return issueScreenTabPaneDao.save(issueScreenTabPane);
  }

  public Boolean issueScreenTabPaneService(Long id) {
    issueScreenTabPaneDao.deleteById(id);
    return true;
  }

  /**
   * @ClassName: IssueScreenTabPaneService @Description: 根据页面id，查询域
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public List<IssueScreenTabPane> getTabPane(Long id) {
    return issueScreenTabPaneDao.findAll(
        Example.of(
            IssueScreenTabPane.builder()
                .issueScreen(IssueScreen.builder().id(id).build())
                .build()));
  }

  /**
   * @ClassName: IssueScreenTabPaneService @Description: 删除TabPane
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Boolean removeIssueScreenTabPane(Long id) {
    // 根据TabPane的id，查找全部字段
    List<FieldToScreen> all =
        fieldToScreenDao.findAll(
            Example.of(
                FieldToScreen.builder()
                    .tabPane(IssueScreenTabPane.builder().id(id).build())
                    .build()));
    if (all.size() > 0) {
      // 首先删除页面与字段表中的数据
      fieldToScreenDao.deleteAll(all);
    }
    // 删除TabPane中的数据
    issueScreenTabPaneDao.deleteById(id);
    return true;
  }
}
