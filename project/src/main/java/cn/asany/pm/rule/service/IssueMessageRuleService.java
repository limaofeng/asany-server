package cn.asany.pm.rule.service;

import cn.asany.pm.rule.bean.IssueMessageRule;
import cn.asany.pm.rule.dao.IssueMessageRuleDao;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IssueMessageRuleService {

  @Autowired private IssueMessageRuleDao issueMessageRuleDao;

  /** 查询所有 */
  public List<IssueMessageRule> findAll() {
    return issueMessageRuleDao.findAll();
  }

  @Transactional
  public Boolean updateIssueMessageRule(Long id, Boolean merge, IssueMessageRule issueMessageRule) {
    issueMessageRule.setId(id);
    issueMessageRuleDao.update(issueMessageRule, merge);
    return true;
  }
}
