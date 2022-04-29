package cn.asany.base.openapi.service;

import cn.asany.base.openapi.apis.AmapOpenAPI;
import cn.asany.base.openapi.bean.OpenApiConfig;
import org.springframework.stereotype.Component;

@Component
public class OpenAPIService {

  private final OpenApiConfigService openApiConfigService;

  public OpenAPIService(OpenApiConfigService openApiConfigService) {
    this.openApiConfigService = openApiConfigService;
  }

  public AmapOpenAPI getDefaultAmap() {
    OpenApiConfig config = openApiConfigService.getAmapDefault();
    assert config != null;
    return new AmapOpenAPI(config.toConfig());
  }
}
