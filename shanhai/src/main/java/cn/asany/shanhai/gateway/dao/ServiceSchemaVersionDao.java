package cn.asany.shanhai.gateway.dao;

import cn.asany.shanhai.gateway.domain.ServiceSchemaVersion;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceSchemaVersionDao extends JpaRepository<ServiceSchemaVersion, Long> {}
