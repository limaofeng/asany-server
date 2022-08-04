package cn.asany.pm.issue.type.service;

import cn.asany.pm.issue.type.dao.IssueTypeDao;
import cn.asany.pm.issue.type.dao.IssueTypeSchemeDao;
import cn.asany.pm.issue.type.domain.IssueType;
import cn.asany.pm.issue.type.domain.IssueTypeScheme;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 任务类型
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class IssueTypeService {

  @Autowired private IssueTypeDao taskTypeDao;
  @Autowired private IssueTypeSchemeDao issueTypeSchemeDao;

  /**
   * 添加任务类型
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public IssueType createTaskType(Long schemId, Long categoryId, IssueType issueType) {
    IssueTypeScheme model = issueTypeSchemeDao.getOne(schemId);
    issueType = taskTypeDao.save(issueType);
    model.getTypes().add(issueType);
    issueTypeSchemeDao.save(model);
    //    IssueTypeCategory category = issueTypeCategoryDao.getOne(categoryId);
    //    List<IssueType> issueTypes = category.getIssueTypes();
    //    issueTypes.add(issueType);
    //    issueTypeCategoryDao.save(category);
    return issueType;
  }

  /**
   * 更新任务类型
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  @Transactional
  public IssueType updateTaskType(Long id, Boolean merge, IssueType taskType) {
    taskType.setId(id);
    return taskTypeDao.update(taskType, merge);
  }

  /**
   * 删除任务类型
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public void removeTaskType(Long id) {
    taskTypeDao.deleteById(id);
  }

  /** * 查询全部任务类型 */
  public List<IssueType> issueTypes() {
    return taskTypeDao.findAll();
  }

  /** * 查询单个任务类型 */
  public IssueType findById(Long id) {
    return taskTypeDao.findById(id).orElse(null);
  }
}
