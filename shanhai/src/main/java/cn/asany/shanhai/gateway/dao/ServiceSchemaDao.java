package cn.asany.shanhai.gateway.dao;

import cn.asany.shanhai.gateway.domain.ServiceSchema;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServiceSchemaDao extends AnyJpaRepository<ServiceSchema, Long> {}
