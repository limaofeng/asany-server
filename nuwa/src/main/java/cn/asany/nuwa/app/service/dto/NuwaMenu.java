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

import cn.asany.nuwa.app.domain.enums.MenuType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 导入导出对象
 *
 * @author limaofeng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NuwaMenu {
  private String name;
  private MenuType type;
  private String icon;
  private String path;
  private NuwaComponent component;
  private List<NuwaMenu> children;
  private Boolean hideInBreadcrumb;
  private Boolean hideChildrenInMenu;
  private Boolean hideInMenu;
}
