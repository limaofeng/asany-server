package cn.asany.pm.rule.dao;

import cn.asany.pm.rule.bean.IssueMessageRule;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueMessageRuleDao extends AnyJpaRepository<IssueMessageRule, Long> {}
