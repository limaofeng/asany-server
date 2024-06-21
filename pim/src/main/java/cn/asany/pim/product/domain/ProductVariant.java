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

import cn.asany.cms.article.domain.ArticleTag;
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
@Table(name = "PIM_PRODUCT_VARIANT")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductVariant extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @TableGenerator
  private Long id;

  @Column(name = "NAME", length = 100, nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "PRODUCT_ID", foreignKey = @ForeignKey(name = "FK_PIM_PRODUCT_MODEL_PID"))
  private Product product;

  @ManyToMany(targetEntity = ArticleTag.class, fetch = FetchType.LAZY)
  @JoinTable(
      name = "PIM_PRODUCT_MODEL_VARIANT",
      joinColumns =
          @JoinColumn(
              name = "MODEL_ID",
              foreignKey = @ForeignKey(name = "FK_PIM_PRODUCT_MODEL_VARIANT_MID")),
      inverseJoinColumns =
          @JoinColumn(
              name = "VARIANT_ID",
              foreignKey = @ForeignKey(name = "FK_PIM_PRODUCT_MODEL_VARIANT_VID")))
  private List<ProductVariant> variants;
}
