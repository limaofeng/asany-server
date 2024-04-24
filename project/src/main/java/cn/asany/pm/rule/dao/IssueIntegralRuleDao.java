package cn.asany.pm.rule.dao;

import cn.asany.pm.rule.bean.IssueIntegralRule;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12 9:12
 */
@Repository
public interface IssueIntegralRuleDao extends AnyJpaRepository<IssueIntegralRule, Long> {}
