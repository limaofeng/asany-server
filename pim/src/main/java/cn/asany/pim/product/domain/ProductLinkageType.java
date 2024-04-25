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
package cn.asany.pim.product.domain;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(
    name = "PIM_PRODUCT_ARTICLE_LINKAGE_TYPE",
    uniqueConstraints = {
      @UniqueConstraint(
          columnNames = {"PRODUCT_ID", "CODE"},
          name = "UK_PIM_PRODUCT_ARTICLE_LINKAGE_TYPE_PID_CODE")
    })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductLinkageType extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @TableGenerator
  private Long id;

  /** 关联类型编码 */
  @Column(name = "CODE", length = 20, nullable = false)
  private String code;

  /** 关联类型名称 */
  @Column(name = "NAME", length = 20, nullable = false)
  private String name;

  /** 关联产品 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "PRODUCT_ID",
      foreignKey = @ForeignKey(name = "FK_PIM_PRODUCT_ARTICLE_LINKAGE_TYPE_PID"))
  private Product product;
}
