package cn.asany.pm.issue.core.service;

import cn.asany.pm.issue.core.dao.FollowDao;
import cn.asany.pm.issue.core.domain.Follow;
import cn.asany.pm.issue.core.domain.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Service
public class FollowService {
  @Autowired private FollowDao followDao;

  public Boolean watchIssue(Long id, Follow issueTaskFollow) {
    issueTaskFollow.setIssue(Issue.builder().id(id).build());
    followDao.save(issueTaskFollow);
    return true;
  }

  public Boolean unwatchIssue(Long issueFollow) {
    followDao.deleteById(issueFollow);
    return true;
  }
}
