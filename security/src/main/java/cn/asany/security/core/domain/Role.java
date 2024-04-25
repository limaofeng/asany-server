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

import cn.asany.security.core.domain.enums.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.Tenantable;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

/**
 * 角色
 *
 * @author limaofeng
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@Entity
@Table(name = "AUTH_ROLE")
@JsonIgnoreProperties({
  "hibernateLazyInitializer",
  "handler",
})
public class Role extends BaseBusEntity implements Tenantable {

  @Transient
  public static final Role USER = Role.builder().id(1L).name("USER").description("普通用户").build();

  @Transient
  public static final Role ADMIN = Role.builder().id(2L).name("ADMIN").description("管理员").build();

  @Id
  @Column(name = "ID")
  @TableGenerator
  private Long id;

  /** 角色名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 描述信息 */
  @Column(name = "DESCRIPTION", length = 250)
  private String description;

  /** 角色类型 */
  @Enumerated(EnumType.STRING)
  @Column(name = "TYPE", length = 20, nullable = false)
  private RoleType type;

  /** 信任的实体 */
  @Embedded TrustedEntity trustedEntity;

  /** 角色使用范围 */
  @OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
  @ToString.Exclude
  private List<RoleScope> scopes;

  /** 租户ID */
  @Column(name = "TENANT_ID", length = 24)
  private String tenantId;
}
