package cn.asany.cms.article.bean;

import cn.asany.cms.article.bean.enums.ArticleRecommendStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import lombok.*;
import org.jfantasy.framework.dao.BaseBusEntity;
import org.jfantasy.framework.lucene.annotations.Indexed;

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
@Table(name = "CMS_ARTICLE_RECOMMEND")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ArticleRecommend extends BaseBusEntity {
  @Id
  @Column(name = "ID", length = 10)
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "ARTICLE_RECOMMEND_GEN")
  @TableGenerator(
      name = "ARTICLE_RECOMMEND_GEN",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "article_recommend_gen:id",
      valueColumnName = "gen_value")
  private Long id;
  /** 推荐状态 */
  @Enumerated(EnumType.STRING)
  @Column(name = "STATUS", length = 20, nullable = false)
  private ArticleRecommendStatus status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ARTICLE_ID",
      foreignKey = @ForeignKey(name = "FK_CMS_ARTICLE_RECOMMEND_ARTICLE_ID"),
      nullable = false)
  private Article article;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "RECOMMEND_ID",
      foreignKey = @ForeignKey(name = "FK_CMS_ARTICLE_RECOMMEND_RECOMMEND_ID"),
      updatable = false,
      nullable = false)
  private Recommend recommend;
  /** 时效 推荐天数 */
  @Column(name = "aging")
  private Integer aging;
}
