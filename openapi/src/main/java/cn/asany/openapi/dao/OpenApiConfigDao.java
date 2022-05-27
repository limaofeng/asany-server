package cn.asany.openapi.dao;

import cn.asany.openapi.domain.OpenApiConfig;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenApiConfigDao extends JpaRepository<OpenApiConfig, Long> {}
