package cn.asany.pim.product.domain;

import cn.asany.cms.article.domain.Article;
import jakarta.persistence.*;
import lombok.*;
import net.asany.jfantasy.framework.dao.BaseBusEntity;
import net.asany.jfantasy.framework.dao.hibernate.annotations.TableGenerator;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "PIM_PRODUCT_ARTICLE")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProductArticle extends BaseBusEntity {

  @Id
  @Column(name = "ID", nullable = false, updatable = false)
  @TableGenerator
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
