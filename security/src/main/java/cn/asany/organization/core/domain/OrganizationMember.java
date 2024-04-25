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
package cn.asany.organization.core.domain;

import cn.asany.security.core.domain.Role;
import cn.asany.security.core.domain.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/** 组织成员 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(
    name = "ORG_ORGANIZATION_MEMBER",
    uniqueConstraints =
        @UniqueConstraint(
            columnNames = {"USER_ID", "ORGANIZATION_ID"},
            name = "UK_ORGANIZATION_MEMBER_OUID"))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "children", "employees"})
public class OrganizationMember extends BaseBusEntity {
  @Id
  @Column(name = "ID")
  @TableGenerator
  private Long id;

  /** 角色 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ROLE_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_ORGANIZATION_MEMBER_ROLE"))
  @ToString.Exclude
  private Role role;

  /** 用户 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "USER_ID",
      nullable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "FK_ORGANIZATION_MEMBER_USER"))
  @ToString.Exclude
  private User user;

  /** 团队ID */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ORGANIZATION_ID",
      nullable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "FK_ORGANIZATION_MEMBER_ORG"))
  @ToString.Exclude
  private Organization organization;
}
