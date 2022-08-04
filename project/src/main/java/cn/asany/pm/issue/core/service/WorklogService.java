package cn.asany.pm.issue.core.service;

import cn.asany.pm.issue.core.dao.WorklogDao;
import cn.asany.pm.issue.core.domain.Issue;
import cn.asany.pm.issue.core.domain.Worklog;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Service
public class WorklogService {
  @Autowired private WorklogDao worklogDao;

  public Worklog save(Long id, Worklog issueTaskLog) {
    issueTaskLog.setIssue(Issue.builder().id(id).build());
    issueTaskLog.setLogTime(new Date());
    return worklogDao.save(issueTaskLog);
  }
}
