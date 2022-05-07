package cn.asany.openapi.service;

import cn.asany.openapi.apis.AmapOpenAPI;
import cn.asany.openapi.bean.OpenApiConfig;
import cn.asany.openapi.configs.AmapApiConfig;
import cn.asany.openapi.configs.WeixinConfig;
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
    return new AmapOpenAPI(config.toConfig(AmapApiConfig.class));
  }

  public WeixinConfig getDefaultWeixin() {
    OpenApiConfig config = openApiConfigService.getDefaultWeixin();
    if (config == null) {
      return null;
    }
    return config.toConfig(WeixinConfig.class);
  }
}
