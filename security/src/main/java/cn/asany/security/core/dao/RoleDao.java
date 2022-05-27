package cn.asany.security.core.dao;

import cn.asany.security.core.domain.Role;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/** @author limaofeng */
@Repository("fantasy.auth.hibernate.RoleDao")
public interface RoleDao extends JpaRepository<Role, Long> {}
