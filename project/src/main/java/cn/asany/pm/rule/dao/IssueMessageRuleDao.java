package cn.asany.pm.rule.dao;

import cn.asany.pm.rule.bean.IssueMessageRule;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueMessageRuleDao extends JpaRepository<IssueMessageRule, Long> {}
