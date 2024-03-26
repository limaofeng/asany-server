package cn.asany.pim.product.domain;

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
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
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
