package cn.asany.pm.rule.dao;

import cn.asany.pm.rule.bean.IssueAppraisalRuleInfo;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueAppraisalRuleInfoDao extends AnyJpaRepository<IssueAppraisalRuleInfo, Long> {}
