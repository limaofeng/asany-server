package cn.asany.workflow.state.service;

import java.util.List;
import net.whir.hos.issue.attribute.bean.IssueResolution;
import net.whir.hos.issue.attribute.dao.IssueResolutionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** @Author: fengmeng @Date: 2019/5/22 20:12 */
@Service
public class IssueResolutionService {

  @Autowired private IssueResolutionDao issueResolutionDao;

  public List<IssueResolution> findAll() {
    return issueResolutionDao.findAll();
  }
}
