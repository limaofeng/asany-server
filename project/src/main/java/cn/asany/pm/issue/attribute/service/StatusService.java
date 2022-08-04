package cn.asany.pm.issue.attribute.service;

import cn.asany.pm.issue.attribute.dao.StatusDao;
import cn.asany.pm.issue.attribute.domain.Status;
import java.util.List;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 状态服务
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StatusService {

  private final StatusDao statusDao;

  public StatusService(StatusDao statusDao) {
    this.statusDao = statusDao;
  }

  /**
   * 添加状态
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Status createIssueState(Status issueState) {
    return statusDao.save(issueState);
  }

  /**
   * 修改状态
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Status updateIssueState(Long id, Boolean merge, Status issueState) {
    issueState.setId(id);
    return statusDao.update(issueState, merge);
  }

  /**
   * 删除状态
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Boolean removeStatus(Long id) {
    statusDao.deleteById(id);
    return true;
  }

  /**
   * 保存数据
   *
   * @author limaofeng@msn.com
   * @date 2022/7/28 9:12
   */
  public Status save(Status Status) {
    return statusDao.save(Status);
  }

  public Page<Status> findPage(Pageable pageable, List<PropertyFilter> filters) {
    return statusDao.findPage(pageable, filters);
  }
}
