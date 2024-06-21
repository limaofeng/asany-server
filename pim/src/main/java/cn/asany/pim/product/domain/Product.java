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

import cn.asany.pim.core.domain.WarrantyPolicy;
import jakarta.persistence.*;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "PIM_PRODUCT")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @TableGenerator
  private Long id;

  /** 产品名称 */
  @Column(name = "NAME", length = 100, nullable = false)
  private String name;

  /** 品牌 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "BRAND_ID", foreignKey = @ForeignKey(name = "FK_PIM_PRODUCT_BRAND_ID"))
  private Brand brand;

  /** 产品规格 */
  @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
  private List<ProductSpecification> specifications;

  /** 产品保修策略 */
  @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
  private List<WarrantyPolicy> warrantyPolicies;

  /** 产品文章 */
  @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
  private List<ProductArticle> articles;

  /** 产品图片 */
  @OneToMany(
      mappedBy = "product",
      cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  @OrderBy("index ASC")
  private List<ProductImage> images;
}
