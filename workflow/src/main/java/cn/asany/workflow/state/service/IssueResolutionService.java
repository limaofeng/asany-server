package cn.asany.workflow.state.service;

import java.util.List;
import cn.asany.pm.attribute.bean.IssueResolution;
import cn.asany.pm.attribute.dao.IssueResolutionDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** @author limaofeng@msn.com @date 2022/7/28 9:12 9:12
@Service
public class IssueResolutionService {

  @Autowired private IssueResolutionDao issueResolutionDao;

  public List<IssueResolution> findAll() {
    return issueResolutionDao.findAll();
  }
}
