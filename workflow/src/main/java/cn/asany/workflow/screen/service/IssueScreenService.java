package cn.asany.workflow.screen.service;

import java.util.List;
import net.whir.hos.issue.screen.bean.IssueScreen;
import net.whir.hos.issue.screen.dao.IssueScreenDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author penghanying @ClassName: IssueScreenService @Description: 页面的service(这里用一句话描述这个类的作用)
 * @date 2019/5/23
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IssueScreenService {

  // 页面的Service
  @Autowired private IssueScreenDao issueScreenDao;

  /**
   * @ClassName: IssueScreenService @Description: 添加页面
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public IssueScreen createIssueScreen(IssueScreen issueScreen) {
    return issueScreenDao.save(issueScreen);
  }

  /**
   * @ClassName: IssueScreenService @Description: 修改页面
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public IssueScreen updateIssueScreen(Long id, Boolean merge, IssueScreen issueScreen) {
    issueScreen.setId(id);
    return issueScreenDao.update(issueScreen, merge);
  }

  /**
   * @ClassName: IssueScreenService @Description: 删除页面
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public Boolean removeIssueScreen(Long id) {
    issueScreenDao.deleteById(id);
    return true;
  }

  /**
   * @ClassName: IssueScreenService @Description: 查询全部页面
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public List<IssueScreen> screens() {
    return issueScreenDao.findAll();
  }

  /**
   * @ClassName: IssueScreenService @Description: 查询某个页面分配的全部字段
   *
   * @author penghanying
   * @date 2019/5/23
   */
  public IssueScreen screen(Long id) {
    return issueScreenDao.findById(id).orElse(null);
  }
}
