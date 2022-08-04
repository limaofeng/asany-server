package cn.asany.pm.rule.dao;

import cn.asany.pm.rule.bean.IssueRuleScheme;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Repository
public interface IssueRuleSchemeDao extends JpaRepository<IssueRuleScheme, Long> {}
