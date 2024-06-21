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

import cn.asany.organization.core.domain.Organization;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.SnowflakeGenerator;

@Data
@Builder
@Entity
@Table(name = "SYS_TENANT")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Tenant extends BaseBusEntity {

  @Id
  @Column(name = "ID", length = 32)
  @SnowflakeGenerator
  private String id;

  @Column(name = "DOMAIN", length = 32)
  private String domain;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ACCOUNT_ID",
      referencedColumnName = "ID",
      foreignKey = @ForeignKey(name = "FK_TENANT_ACCOUNT_ID"))
  private User mainAccount;

  @OneToOne(mappedBy = "tenant", fetch = FetchType.LAZY)
  private AccessControlSettings accessControlSettings;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "DEFAULT_ORGANIZATION_ID", referencedColumnName = "ID", nullable = false)
  private Organization defaultOrganization;
}
