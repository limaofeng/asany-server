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

import cn.asany.security.core.domain.converter.ConditionConverter;
import cn.asany.security.core.domain.enums.PermissionPolicyEffect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;
import lombok.*;
import net.asany.jfantasy.framework.dao.Tenantable;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import net.asany.jfantasy.framework.dao.hibernate.converter.StringArrayConverter;

/**
 * 权限策略声明
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
@Table(name = "AUTH_PERMISSION_STATEMENT")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class PermissionStatement implements Serializable, Tenantable {

  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @TableGenerator
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "EFFECT", length = 20)
  private PermissionPolicyEffect effect;

  @Column(name = "ACTION", columnDefinition = "JSON")
  @Convert(converter = StringArrayConverter.class)
  private String[] action;

  @Column(name = "RESOURCE", columnDefinition = "JSON")
  @Convert(converter = StringArrayConverter.class)
  private String[] resource;

  @Column(name = "`CONDITION`", columnDefinition = "JSON")
  @Convert(converter = ConditionConverter.class)
  private List<PermissionCondition> condition;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "PERMISSION_POLICY",
      foreignKey = @ForeignKey(name = "FK_PERMISSION_STATEMENT_POLICY_ID"),
      nullable = false)
  private PermissionPolicy policy;

  @Column(name = "TENANT_ID", length = 24, updatable = false)
  private String tenantId;

  public PermissionStatement(PermissionPolicyEffect effect, String action, String resource) {
    this.effect = effect;
    this.action = new String[] {action};
    this.resource = new String[] {resource};
  }
}
