package cn.asany.pm.rule.dao;

import cn.asany.pm.rule.bean.IssueAppraisalRule;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueAppraisalRuleDao extends JpaRepository<IssueAppraisalRule, Long> {}
