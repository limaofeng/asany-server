package cn.asany.pm.issue.type.service;

import cn.asany.pm.issue.type.dao.IssueTypeDao;
import cn.asany.pm.issue.type.dao.IssueTypeSchemeDao;
import cn.asany.pm.issue.type.domain.IssueTypeScheme;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 任务类型方案
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IssueTypeSchemeService {

  @Autowired private IssueTypeSchemeDao issueTypeSchemeDao;

  @Autowired private IssueTypeDao issueTypeDao;

  /** 编辑任务类型方案 */
  public IssueTypeScheme updateIssueTypeScheme(
      Long id, Boolean merge, IssueTypeScheme issueTypeScheme) {
    issueTypeScheme.setId(id);
    return issueTypeSchemeDao.update(issueTypeScheme, merge);
  }

  /** 删除任务类型方案 */
  public Boolean removeIssueTypeScheme(Long id) {
    issueTypeSchemeDao.deleteById(id);
    return true;
  }

  /**
   * 查询全部任务类型方案
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public List<IssueTypeScheme> issueTypeSchemes() {
    return issueTypeSchemeDao.findAll();
  }

  /**
   * 新建任务类型方案
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public IssueTypeScheme createIssueTypeScheme(IssueTypeScheme issueTypeScheme) {
    return issueTypeSchemeDao.save(issueTypeScheme);
  }
}
