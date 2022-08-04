package cn.asany.pm.issue.core.service;

import cn.asany.pm.issue.core.dao.IssueTimeTrackDao;
import cn.asany.pm.issue.core.domain.TimeTrack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Service
public class TimeTrackService {
  @Autowired private IssueTimeTrackDao issueTimeTrackDao;

  public TimeTrack save(TimeTrack timeTrack) {
    return issueTimeTrackDao.save(timeTrack);
  }
}
