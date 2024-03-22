package cn.asany.pim.product.domain;

import cn.asany.cms.article.domain.ArticleTag;
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
@Table(name = "PIM_PRODUCT_VARIANT")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductVariant extends BaseBusEntity {
  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
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
