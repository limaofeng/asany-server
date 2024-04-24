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
