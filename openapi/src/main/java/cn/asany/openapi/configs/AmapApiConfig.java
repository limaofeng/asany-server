package cn.asany.openapi.configs;

import cn.asany.openapi.IOpenApiConfig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AmapApiConfig implements IOpenApiConfig {
  private String key;
}
