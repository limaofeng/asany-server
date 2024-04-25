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

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.annotations.Cache;

/**
 * 资源类型
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
@Table(
    name = "AUTH_RESOURCE_TYPE",
    uniqueConstraints = @UniqueConstraint(name = "UK_RESOURCE_TYPE_LABEL", columnNames = "LABEL"))
@Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class ResourceType extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false, precision = 22)
  @TableGenerator
  private Long id;

  /** 标签 */
  @Column(name = "LABEL", length = 150, nullable = false)
  private String label;

  /** 名称 */
  @Column(name = "NAME", length = 50)
  private String name;

  /** 描述 */
  @Column(name = "DESCRIPTION", length = 1024)
  private String description;

  /** 资源名称 */
  @Column(name = "RESOURCE_NAME", length = 50)
  private String resourceName;

  /** 资源的ARN格式 */
  @ElementCollection
  @CollectionTable(
      name = "AUTH_RESOURCE_TYPE_ARN",
      foreignKey = @ForeignKey(name = "FK_RESOURCE_TYPE_ARN_RESOURCE_ID"),
      joinColumns = @JoinColumn(name = "RESOURCE_TYPE"))
  @Column(name = "ARN", length = 250)
  private Set<String> arns;

  /** 服务 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "SERVICE_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_RESOURCE_TYPE_SERVICE"))
  private AuthorizedService service;

  @SuppressWarnings("unused")
  public static class ResourceTypeBuilder {

    public ResourceTypeBuilder arn(String arn) {
      if (this.arns == null) {
        this.arns = new HashSet<>();
      }
      this.arns.add(arn);
      return this;
    }
  }
}
