package cn.asany.pim.core.domain;

import cn.asany.pim.product.domain.Product;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;

@Data
@Entity
@Table(name = "PIM_WARRANTY_POLICY")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WarrantyPolicy extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @TableGenerator
  private Long id;

  @Column(name = "NAME", length = 20, nullable = false, unique = true)
  private String name;

  /** 有效期限，以月为单位 */
  @Column(name = "DURATION", nullable = false)
  private Integer duration;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "PRODUCT_ID",
      foreignKey = @ForeignKey(name = "FK_PIM_WARRANTY_POLICY_PRODUCT"))
  private Product product;
}
