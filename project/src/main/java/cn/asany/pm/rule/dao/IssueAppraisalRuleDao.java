package cn.asany.pm.rule.dao;

import cn.asany.pm.rule.bean.IssueAppraisalRule;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueAppraisalRuleDao extends AnyJpaRepository<IssueAppraisalRule, Long> {}
