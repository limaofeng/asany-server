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

import cn.asany.security.core.domain.enums.GranteeType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.Tenantable;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(
    name = "AUTH_GROUP_MEMBER",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "UK_AUTH_GROUP_MEMBER",
          columnNames = {"GROUP_ID", "TYPE", "VALUE"})
    })
public class GroupMember extends BaseBusEntity implements Tenantable {
  @Id
  @Column(name = "ID")
  @TableGenerator
  private Long id;

  /** 成员类型 */
  @Column(name = "TYPE", length = 24)
  private GranteeType type;

  /** 成员ID */
  @Column(name = "VALUE", length = 24)
  private String value;

  /** 租户ID */
  @Column(name = "TENANT_ID", length = 24, nullable = false, updatable = false)
  private String tenantId;

  /** 用户组 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "GROUP_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_GROUP_MEMBER_UID"))
  private Group group;
}
