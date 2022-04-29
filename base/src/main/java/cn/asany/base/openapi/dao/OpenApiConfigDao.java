package cn.asany.base.openapi.dao;

import cn.asany.base.openapi.bean.OpenApiConfig;
import org.jfantasy.framework.dao.jpa.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OpenApiConfigDao extends JpaRepository<OpenApiConfig, Long> {}
