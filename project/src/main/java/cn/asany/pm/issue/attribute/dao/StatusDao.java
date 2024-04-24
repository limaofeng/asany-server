package cn.asany.pm.issue.attribute.dao;

import cn.asany.pm.issue.attribute.domain.Status;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 状态 Dao
 *
 * @author limaofeng@msn.com
 * @date 2022/7/28 9:12
 */
@Repository
public interface StatusDao extends AnyJpaRepository<Status, Long> {}
