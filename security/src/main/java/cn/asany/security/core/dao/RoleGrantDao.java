package cn.asany.security.core.dao;

import cn.asany.security.core.domain.RoleGrant;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 角色授权
 *
 * @author limaofeng
 */
@Repository
public interface RoleGrantDao extends AnyJpaRepository<RoleGrant, String> {}
