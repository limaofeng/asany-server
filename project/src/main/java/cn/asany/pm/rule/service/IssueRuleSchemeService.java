package cn.asany.pm.rule.service;

import cn.asany.pm.rule.bean.IssueRuleScheme;
import cn.asany.pm.rule.dao.IssueRuleSchemeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Service
public class IssueRuleSchemeService {
  @Autowired private IssueRuleSchemeDao issueRuleSchemeDao;

  /** 更新规则方案 */
  @Transactional
  public Boolean updateIssueRuleScheme(Long id, Boolean merge, IssueRuleScheme issueRuleScheme) {
    issueRuleScheme.setId(id);
    issueRuleSchemeDao.update(issueRuleScheme, merge);
    return true;
  }
}
