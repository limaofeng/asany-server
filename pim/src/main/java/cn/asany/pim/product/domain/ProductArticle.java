package cn.asany.pim.product.domain;

import cn.asany.cms.article.domain.Article;
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
@Table(name = "PIM_PRODUCT_ARTICLE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductArticle extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 关联类型 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "LINKAGE_TYPE",
      foreignKey = @ForeignKey(name = "FK_PIM_PRODUCT_ARTICLE_LINKAGE_TYPE_ID"))
  private ProductLinkageType linkageType;
  /** 产品 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "PRODUCT_ID",
      foreignKey = @ForeignKey(name = "FK_PIM_PRODUCT_ARTICLE_PID"),
      nullable = false)
  private Product product;
  /** 文章 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ARTICLE_ID",
      foreignKey = @ForeignKey(name = "FK_PIM_PRODUCT_ARTICLE_AID"),
      nullable = false)
  private Article article;
}
