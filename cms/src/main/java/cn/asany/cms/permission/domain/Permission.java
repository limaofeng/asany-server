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
package cn.asany.cms.permission.domain;

import cn.asany.cms.permission.domain.enums.PermissionCategory;
import java.util.List;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;

/**
 * 权限配置信息
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(of = "id", callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Permission extends BaseBusEntity {

  private String id;

  /** 权限名称 */
  private String name;

  /** 资源描述 */
  private String description;

  /** 是否启用 */
  private Boolean enabled;

  /** 类型 */
  private PermissionCategory category;

  /** 资源类型 */
  private String resourceType;

  private List<GrantPermission> grants;

  /** 对应的权限分类 */
  private PermissionType permissionType;
}
