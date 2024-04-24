package cn.asany.security.core.dao;

import cn.asany.security.core.domain.Group;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 用户组 DAO
 *
 * @author limaofeng
 */
@Repository
public interface GroupDao extends AnyJpaRepository<Group, Long> {}
