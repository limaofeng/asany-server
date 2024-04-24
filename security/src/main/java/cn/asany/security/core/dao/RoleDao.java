package cn.asany.security.core.dao;

import cn.asany.security.core.domain.Role;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author limaofeng
 */
@Repository("fantasy.auth.hibernate.RoleDao")
public interface RoleDao extends AnyJpaRepository<Role, Long> {}
