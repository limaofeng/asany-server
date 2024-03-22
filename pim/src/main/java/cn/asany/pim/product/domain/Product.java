package cn.asany.pim.product.domain;

import cn.asany.pim.core.domain.WarrantyPolicy;
import java.util.List;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "PIM_PRODUCT")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Product extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;

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
}
