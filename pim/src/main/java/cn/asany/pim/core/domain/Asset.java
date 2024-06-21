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
package cn.asany.pim.core.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Data
@Entity
@Table(
    name = "PIM_ASSET",
    uniqueConstraints = {
      @UniqueConstraint(
          columnNames = {"PRODUCT_ID", "SN"},
          name = "UK_PIM_ASSET_SN")
    })
@EqualsAndHashCode(callSuper = true)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "ASSET_TYPE", discriminatorType = DiscriminatorType.STRING)
public abstract class Asset extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @TableGenerator
  private Long id;

  /** 资产编号 */
  @Column(name = "NO", length = 150)
  private String no;

  /** 资产名称 */
  @Column(name = "NAME", length = 100)
  private String name;

  /** 资产类型 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ASSET_TYPE",
      foreignKey = @ForeignKey(name = "FK_PIM_ASSET_TYPE"),
      insertable = false,
      updatable = false)
  private AssetType type;

  /** 资产的当前状态，如在用1、维修中、已报废等。 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "STATUS_ID", foreignKey = @ForeignKey(name = "FK_PIM_ASSET_STATUS"))
  private AssetStatus status;
}
