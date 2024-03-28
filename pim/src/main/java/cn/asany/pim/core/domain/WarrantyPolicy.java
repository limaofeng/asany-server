package cn.asany.pim.core.domain;

import cn.asany.pim.product.domain.Product;
import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;

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
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
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
