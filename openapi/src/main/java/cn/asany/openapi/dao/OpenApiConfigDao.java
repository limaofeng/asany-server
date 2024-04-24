package cn.asany.openapi.dao;

import cn.asany.openapi.domain.OpenApiConfig;
import net.asany.jfantasy.framework.dao.jpa.AnyJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenApiConfigDao extends AnyJpaRepository<OpenApiConfig, Long> {}
