package cn.asany.pm.rule.service;

import cn.asany.pm.rule.bean.IssueAppraisalRuleInfo;
import cn.asany.pm.rule.dao.IssueAppraisalRuleInfoDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IssueAppraisalRuleInfoService {

  @Autowired private IssueAppraisalRuleInfoDao issueAppraisalRuleInfoDao;

  public List<IssueAppraisalRuleInfo> findAll() {
    return issueAppraisalRuleInfoDao.findAll();
  }
}
