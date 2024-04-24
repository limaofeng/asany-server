package cn.asany.security.core.dao;

import cn.asany.security.core.domain.Tenant;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantDao extends AnyJpaRepository<Tenant, String> {}
