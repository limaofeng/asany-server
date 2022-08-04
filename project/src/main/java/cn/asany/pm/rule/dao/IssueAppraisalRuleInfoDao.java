package cn.asany.pm.rule.dao;

import cn.asany.pm.rule.bean.IssueAppraisalRuleInfo;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IssueAppraisalRuleInfoDao extends JpaRepository<IssueAppraisalRuleInfo, Long> {}
