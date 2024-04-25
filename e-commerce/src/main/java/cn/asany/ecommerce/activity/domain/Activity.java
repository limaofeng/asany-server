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
package cn.asany.ecommerce.activity.domain;

import cn.asany.ecommerce.activity.domain.enums.ActivityStatus;
import cn.asany.organization.core.domain.Organization;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import javax.validation.constraints.Null;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import net.asany.jfantasy.framework.spring.validation.Operation;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "EC_ACTIVITY",
    uniqueConstraints =
        @UniqueConstraint(
            name = "UK_EC_ACTIVITY_SLUG",
            columnNames = {"ORGANIZATION_ID", "SLUG"}))
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "target", "comments"})
public class Activity extends BaseBusEntity {

  @Id
  @Null(groups = Operation.Create.class)
  @Column(name = "ID", nullable = false)
  @TableGenerator
  private Long id;

  @Column(name = "SLUG", length = 200)
  private String slug;

  /** 状态 */
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", length = 20, nullable = false)
  private ActivityStatus status;

  @Column(name = "NAME", length = 200)
  private String name;

  @OneToMany(
      mappedBy = "parent",
      fetch = FetchType.LAZY,
      cascade = {CascadeType.REMOVE})
  @OrderBy("index ASC")
  @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
  private List<ActivityMerchant> merchants;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "CATEGORY_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_EC_ACTIVITY_CATEGORY"))
  private ActivityCategory category;

  @ManyToOne(targetEntity = Organization.class, fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ORGANIZATION_ID",
      nullable = false,
      foreignKey = @ForeignKey(name = "FK_EC_ACTIVITY_ORG_ID"))
  @ToString.Exclude
  private Organization organization;
}
