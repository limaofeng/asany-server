package cn.asany.pm.issue.type.dao;

import cn.asany.pm.issue.type.domain.IssueTypeScheme;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng@msn.com @ClassName: TaskTypeScheme @Description: 添加任务类型方案(这里用一句话描述这个类的作用)
 * @date 2022/7/28 9:12
 */
@Repository
public interface IssueTypeSchemeDao extends AnyJpaRepository<IssueTypeScheme, Long> {}
