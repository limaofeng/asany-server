package cn.asany.shanhai.gateway.dao;

import cn.asany.shanhai.gateway.domain.ServiceSchemaVersion;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceSchemaVersionDao extends AnyJpaRepository<ServiceSchemaVersion, Long> {}
