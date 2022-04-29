package cn.asany.base.openapi.service;

import cn.asany.base.openapi.bean.OpenApiConfig;
import cn.asany.base.openapi.bean.enums.OpenApiType;
import cn.asany.base.openapi.dao.OpenApiConfigDao;
import java.util.List;
import org.jfantasy.framework.dao.jpa.PropertyFilter;
import org.springframework.stereotype.Service;

@Service
public class OpenApiConfigService {

  private final OpenApiConfigDao openApiConfigDao;

  public OpenApiConfigService(OpenApiConfigDao openApiConfigDao) {
    this.openApiConfigDao = openApiConfigDao;
  }

  public OpenApiConfig getAmapDefault() {
    List<OpenApiConfig> configs =
        openApiConfigDao.findAll(PropertyFilter.builder().equal("type", OpenApiType.AMAP).build());
    return configs.isEmpty() ? null : configs.get(0);
  }
}
