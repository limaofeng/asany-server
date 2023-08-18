package cn.asany.security.core.dao;

import cn.asany.security.core.domain.Tenant;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantDao extends JpaRepository<Tenant, String> {}
