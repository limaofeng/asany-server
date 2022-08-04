package cn.asany.pm.rule.service;

import cn.asany.pm.rule.bean.IssueAppraisalRule;
import cn.asany.pm.rule.dao.IssueAppraisalRuleDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class IssueAppraisalRuleService {
  @Autowired private IssueAppraisalRuleDao issueAppraisalRuleDao;

  public IssueAppraisalRule save(IssueAppraisalRule issueAppraisalRule) {
    return issueAppraisalRuleDao.save(issueAppraisalRule);
  }

  public List<IssueAppraisalRule> findAll() {
    return issueAppraisalRuleDao.findAll();
  }

  public Boolean update(Long ruleId, Boolean merge, IssueAppraisalRule issueAppraisalRule) {
    issueAppraisalRule.setId(ruleId);
    issueAppraisalRuleDao.update(issueAppraisalRule, merge);
    return true;
  }
}
