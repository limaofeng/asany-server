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
package cn.asany.security.core.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.Tenantable;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * 授权
 *
 * @author limaofeng
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "AUTH_PERMISSION")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Permission extends BaseBusEntity implements Tenantable {

  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @TableGenerator
  private Long id;

  /** 授权范围 */
  @Column(name = "SCOPE", length = 25, nullable = false)
  private String scope;

  /** 授权主体 */
  @Embedded private Grantee grantee;

  /** 授权策略 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "POLICY_ID",
      foreignKey = @ForeignKey(name = "FK_PERMISSION_POLICY_ID"),
      updatable = false,
      nullable = false)
  private PermissionPolicy policy;

  /** 租户ID */
  @Column(name = "TENANT_ID", length = 24, updatable = false)
  private String tenantId;
}
