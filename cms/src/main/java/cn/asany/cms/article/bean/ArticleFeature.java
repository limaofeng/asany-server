package cn.asany.cms.article.bean;

import cn.asany.cms.article.bean.enums.ArticleFeatureStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.lucene.annotations.Indexed;

import javax.persistence.*;

/**
 * 文章推荐位
 *
 * @author limaofeng
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Indexed
@Entity
@Table(name = "CMS_ARTICLE_FEATURE")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ArticleFeature extends BaseBusEntity {
  @Id
  @Column(name = "ID", length = 10)
  @GeneratedValue(generator = "fantasy-sequence")
  @GenericGenerator(name = "fantasy-sequence", strategy = "fantasy-sequence")
  private Long id;
  /** 推荐状态 */
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", length = 20, nullable = false)
  private ArticleFeatureStatus status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ARTICLE_ID",
      foreignKey = @ForeignKey(name = "FK_CMS_ARTICLE_FEATURE_ARTICLE_ID"),
      nullable = false)
  private Article article;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "FEATURE_ID",
      foreignKey = @ForeignKey(name = "FK_CMS_ARTICLE_FEATURE_FID"),
      updatable = false,
      nullable = false)
  private Feature feature;
  /** 时效 推荐天数 */
  @Column(name = "aging")
  private Integer aging;
}
