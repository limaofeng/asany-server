package cn.asany.shanhai.gateway.dao;

import cn.asany.shanhai.gateway.domain.ServiceSchemaVersionPatch;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceSchemaVersionPatchDao
    extends AnyJpaRepository<ServiceSchemaVersionPatch, Long> {}
