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
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.Tenantable;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * 用户组
 *
 * @author limaofeng
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(
    name = "AUTH_GROUP",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "UK_AUTH_GROUP",
          columnNames = {"NAME", "TENANT_ID"})
    })
@JsonIgnoreProperties({
  "hibernate_lazy_initializer",
  "handler",
})
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Group extends BaseBusEntity implements Tenantable {
  @Id
  @Column(name = "ID")
  @TableGenerator
  private Long id;

  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 显示名称 */
  @Column(name = "DISPLAY_NAME", length = 50)
  private String displayName;

  /** 描述 */
  @Column(name = "DESCRIPTION", length = 250)
  private String description;

  /** 租户ID */
  @Column(name = "TENANT_ID", length = 24, nullable = false)
  private String tenantId;

  /** 组内成员 */
  @OneToMany(
      mappedBy = "group",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  private List<GroupMember> members;
}
