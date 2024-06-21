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

import cn.asany.cms.permission.domain.enums.SecurityType;
import jakarta.persistence.Id;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;

/**
 * @author limaofeng
 * @version V1.0 @Description: TODO
 * @date 2022/7/28 9:12 9:12
 */
@Getter
@Setter
@EqualsAndHashCode(
    of = {"securityType", "value", "resource", "permission"},
    callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrantPermission extends BaseBusEntity {

  @Id private Long id;

  /** 授权类型 */
  private SecurityType securityType;

  /** 授权 */
  private String value;

  /** 资源 */
  private String resource;

  /** 权限 */
  private Permission permission;

  /** 权限方案 */
  private PermissionScheme scheme;
}
