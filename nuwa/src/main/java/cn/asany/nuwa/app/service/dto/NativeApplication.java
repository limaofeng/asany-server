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
package cn.asany.nuwa.app.service.dto;

import cn.asany.base.IModuleProperties;
import cn.asany.nuwa.app.domain.enums.ApplicationType;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 原生应用
 *
 * @author limaofeng
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NativeApplication {
  private String name;
  @Builder.Default private ApplicationType type = ApplicationType.Native;
  private String title;
  private String url;
  private String description;
  private String callbackUrl;
  private String setupURL;
  private WebHook webhook;

  private String clientId;
  private String clientSecret;

  private Set<String> authorities;
  private List<NuwaRoute> routes;
  private List<NuwaMenu> menus;

  private List<IModuleProperties> modules;
}
