package cn.asany.security.core.dao;

import cn.asany.security.core.domain.Permission;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

/** @author limaofeng */
@Repository
public interface PermissionDao extends JpaRepository<Permission, Long> {}
