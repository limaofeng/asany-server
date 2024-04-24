package cn.asany.security.core.dao;

import cn.asany.security.core.domain.Permission;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionDao extends AnyJpaRepository<Permission, Long> {}
