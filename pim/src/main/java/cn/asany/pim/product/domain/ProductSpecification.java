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
@Table(name = "PIM_PRODUCT_SPECIFICATION")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductSpecification extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @TableGenerator
  private Long id;

  @Column(name = "NAME", length = 100, nullable = false)
  private String name;

  @Column(name = "VALUE", length = 100, nullable = false)
  private String value;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "PRODUCT_ID",
      foreignKey = @ForeignKey(name = "FK_PIM_PRODUCT_VARIANT_VID"),
      nullable = false)
  private Product product;
}
