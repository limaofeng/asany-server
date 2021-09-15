package cn.asany.cms.special.bean;

import cn.asany.cms.article.bean.Article;
import cn.asany.storage.api.converter.FileObjectConverter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.persistence.*;
import javax.tools.FileObject;
import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.jfantasy.framework.dao.BaseBusEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "CMS_SPECIAL_ARTICLE")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "special", "article"})
public class SpecialArticle extends BaseBusEntity {

  @Id
  //  @Null(groups = RESTful.POST.class)
  @Column(name = "ID", nullable = false, precision = 22)
  @GeneratedValue(strategy = GenerationType.TABLE, generator = "special_article_gen")
  @TableGenerator(
      name = "special_article_gen",
      table = "sys_sequence",
      pkColumnName = "gen_name",
      pkColumnValue = "cms_special_article:id",
      valueColumnName = "gen_value")
  private Long id;
  /** 期数 */
  @Column(name = "PERIODICAL", nullable = false)
  private String periodical;
  /** 标题 */
  @Column(name = "TITLE", nullable = false)
  private String title;
  /** 摘要 */
  @Column(name = "SUMMARY", length = 500, nullable = false)
  private String summary;
  /** 封面 */
  @Column(name = "COVER", length = 500)
  @Convert(converter = FileObjectConverter.class)
  private FileObject cover;
  /** 发布日期 */
  @Column(name = "PUBLISH_DATE", length = 15, nullable = false)
  private String publishDate;
  /** 对应的文章 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "SPECIAL_ID",
      nullable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "FK_SPECIALARTICLE_SPECIAL"))
  private Special special;
  /** 对应的文章 */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
      name = "ARTICLE_ID",
      nullable = false,
      updatable = false,
      foreignKey = @ForeignKey(name = "FK_SPECIALARTICLE_ARTICLE"))
  private Article article;
}
