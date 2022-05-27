package cn.asany.shanhai.gateway.dao;

import cn.asany.shanhai.gateway.domain.ServiceSchemaVersionPatch;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceSchemaVersionPatchDao
    extends JpaRepository<ServiceSchemaVersionPatch, Long> {}
