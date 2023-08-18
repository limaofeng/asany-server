package cn.asany.security.core.dao;

import cn.asany.security.core.domain.RoleGrant;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 角色授权
 *
 * @author limaofeng
 */
@Repository
public interface RoleGrantDao extends JpaRepository<RoleGrant, String> {}
