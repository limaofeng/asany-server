/*
 * Copyright (c) 2024 Asany
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.asany.net/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.asany.openapi.service;

import cn.asany.openapi.apis.AmapOpenAPI;
import cn.asany.openapi.configs.AmapApiConfig;
import cn.asany.openapi.configs.WeixinConfig;
import cn.asany.openapi.domain.OpenApiConfig;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class OpenAPIService {

  private final OpenApiConfigService openApiConfigService;
  private final StringRedisTemplate redisTemplate;

  public OpenAPIService(
      OpenApiConfigService openApiConfigService, StringRedisTemplate redisTemplate) {
    this.openApiConfigService = openApiConfigService;
    this.redisTemplate = redisTemplate;
  }

  public AmapOpenAPI getDefaultAmap() {
    OpenApiConfig config = openApiConfigService.getAmapDefault();
    assert config != null;
    return new AmapOpenAPI(redisTemplate, config.toConfig(AmapApiConfig.class));
  }

  public WeixinConfig getDefaultWeixin() {
    OpenApiConfig config = openApiConfigService.getDefaultWeixin();
    if (config == null) {
      return null;
    }
    return config.toConfig(WeixinConfig.class);
  }
}
